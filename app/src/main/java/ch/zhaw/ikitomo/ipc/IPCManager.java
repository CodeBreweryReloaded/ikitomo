package ch.zhaw.ikitomo.ipc;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class IPCManager implements Closeable {
    private static final Logger LOGGER = Logger.getLogger(IPCManager.class.getName());
    private static final String DEFAULT_WELL_KNOWN_PORT_FILE = "./.ipc-port";
    private static final IPCCommandManager IPC_COMMAND_MANAGER = new IPCCommandManager();

    private Path wellKnownPortFile;

    private IPCServer ipcServer;

    private boolean sendShowSettingsCommand;

    private boolean otherInstanceRunning;

    private Runnable showSettingsListener;

    private CompletableFuture<Boolean> otherInstanceRunningFuture = new CompletableFuture<>();

    IPCManager(Path wellKnownPortFile, Runnable showSettingsListener) {
        this.wellKnownPortFile = Objects.requireNonNull(wellKnownPortFile, "wellKnownPortFile = null");
        this.showSettingsListener = Objects.requireNonNull(showSettingsListener, "showSettingsListener = null");
    }

    public void setSendShowSettingsCommand(boolean sendShowSettingsCommand) {
        this.sendShowSettingsCommand = sendShowSettingsCommand;
    }

    public boolean isSendShowSettingsCommand() {
        return sendShowSettingsCommand;
    }

    void init() {
        // if the well-known port file exists, we assume that another instance is
        // running
        otherInstanceRunning = Files.exists(wellKnownPortFile);
        if (!otherInstanceRunning) {
            startUpIPCServer();
        }
        // while startup up the server we might detected an other instance, thus we
        // check the boolean flag again
        if (otherInstanceRunning) {
            tryToConnectToIPCServer();
        }
        // if we couldn't connect to the server -> try again to create a server and
        // write the well known port file
        if (!otherInstanceRunning) {
            startUpIPCServer();
        }

        // complete the otherInstanceRunningFuture
        otherInstanceRunningFuture.complete(otherInstanceRunning);

        Runtime.getRuntime().addShutdownHook(new Thread(this::deleteWellKnownPortFile));
    }

    private void deleteWellKnownPortFile() {
        if (Files.exists(wellKnownPortFile)) {
            try {
                Files.delete(wellKnownPortFile);
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Could not delete well known port file", e);
            }
        }
    }

    private void tryToConnectToIPCServer() {
        try {
            int port = readPortFromWellKnownPortFile();
            try (var client = new IPCClient(port, null)) {
                onClientConnected(client);
            }
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Couldn't contact ipc server", e);
            otherInstanceRunning = false;
        }
    }

    private void onClientConnected(IPCClient client) throws IOException {
        if (isSendShowSettingsCommand()) {
            client.send(IPCCommand.newShowSettingsCommand());
        }
    }

    private void startUpIPCServer() {
        if (ipcServer != null && !ipcServer.isClosed()) {
            return;
        }
        try {
            ipcServer = new IPCServer();
            ipcServer.setCommandListener(this::onCommandReceived);
            ipcServer.start();
            writeWellKnownPortFile(ipcServer.getPort());
        } catch (IOException e) {
            LOGGER.log(Level.WARNING,
                    "Couldn't start IPC server or write well-known port file at \"" + wellKnownPortFile + "\"", e);
            closeIPCServer();
            otherInstanceRunning = true; // maybe another instance wrote the well known port file before we could -> try
                                         // to connect to it
        }
    }

    private void onCommandReceived(IPCCommand command) {
        if (command.command() == null) {
            LOGGER.warning("Received command with null command type");
            return;
        }
        LOGGER.log(Level.INFO, "Reveived command: {0}", command);
        switch (command.command()) {
            case SHOW_SETTINGS -> showSettingsListener.run();
            default -> LOGGER.log(Level.WARNING, "Received unknown command: {0}", command.command());
        }
    }

    private void closeIPCServer() {
        if (ipcServer != null && !ipcServer.isClosed()) {
            try {
                ipcServer.close();
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Couldn't stop ipc server", e);
            }
        }
    }

    public Path getWellKnownPortFile() {
        return wellKnownPortFile;
    }

    public CompletableFuture<Boolean> isOtherInstanceRunning() {
        return otherInstanceRunningFuture;
    }

    private void writeWellKnownPortFile(int port) throws IOException {
        var wellKnownPortContent = String.valueOf(port).getBytes(StandardCharsets.UTF_8);
        Files.write(wellKnownPortFile, wellKnownPortContent, StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.SYNC);
    }

    private int readPortFromWellKnownPortFile() throws IOException {
        String wellKnownPortContent = new String(Files.readAllBytes(wellKnownPortFile), StandardCharsets.UTF_8);
        try {
            return Integer.parseInt(wellKnownPortContent.trim());
        } catch (NumberFormatException e) {
            throw new IOException("Invalid content in well known port file \"" + wellKnownPortContent + "\"", e);
        }
    }

    @Override
    public void close() throws IOException {
        if (ipcServer != null && !ipcServer.isClosed()) {
            ipcServer.close();
        }
    }

    private static IPCManager instance;

    public static void init(boolean sendShowSettingsCommand, Runnable showSettingsListener) {
        Path wellKnownPortFile = Paths.get(DEFAULT_WELL_KNOWN_PORT_FILE);
        instance = new IPCManager(wellKnownPortFile, showSettingsListener);
        instance.setSendShowSettingsCommand(sendShowSettingsCommand);
    }

    public static IPCManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("IPCManager not initialized");
        }
        return instance;
    }

    static class IPCServer implements Closeable {
        private static final Logger LOGGER = Logger.getLogger(IPCServer.class.getName());
        private ServerSocket server;
        private Thread thread = new Thread(this::runThread, "IPC Server Thread");
        private List<IPCClient> clients = new ArrayList<>();

        private Consumer<IPCCommand> commandListener;
        private Consumer<IPCClient> connectListener;

        public IPCServer() {
        }

        public List<IPCClient> getClients() {
            return new ArrayList<>(clients);
        }

        public void start() throws IOException {
            server = new ServerSocket(0, 50, InetAddress.getLoopbackAddress());
            thread.start();
        }

        private void runThread() {
            try {
                while (!server.isClosed()) {
                    Socket socket = server.accept();
                    var client = new IPCClient(socket);
                    client.setCommandListener(commandListener);
                    clients.add(client);
                    if (connectListener != null) {
                        connectListener.accept(client);
                    }
                }
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Couldn't accept connection", e);
            }
        }

        public void setCommandListener(Consumer<IPCCommand> commandListener) {
            this.commandListener = commandListener;
        }

        public void setConnectListener(Consumer<IPCClient> connectListener) {
            this.connectListener = connectListener;
        }

        public int getPort() {
            return server.getLocalPort();
        }

        public boolean isClosed() {
            return server.isClosed();
        }

        @Override
        public void close() throws IOException {
            for (IPCClient client : clients) {
                try {
                    client.close();
                } catch (IOException e) {
                    LOGGER.log(Level.WARNING, "Couldn't close a client", e);
                }
            }
            if (server != null) {
                server.close();
            }
        }
    }

    static class IPCClient implements Closeable {
        private static final Logger LOGGER = Logger.getLogger(IPCClient.class.getName());
        private Socket socket;
        private BufferedReader in;
        private OutputStream out;
        private Consumer<IPCCommand> commandListener;
        private Thread thread;

        public IPCClient(int port, Consumer<IPCCommand> commandListener) throws IOException {
            this.commandListener = commandListener;
            setupSocket(new Socket(InetAddress.getLoopbackAddress(), port));
        }

        public IPCClient(Socket socket) throws IOException {
            setupSocket(socket);
        }

        private void setupSocket(Socket socket) throws IOException {
            this.socket = socket;
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = socket.getOutputStream();
            this.thread = new Thread(this::runReceiveLoop, "ICP Client Thread");
            this.thread.start();
        }

        public void setCommandListener(Consumer<IPCCommand> commandListener) {
            this.commandListener = commandListener;
        }

        public void send(IPCCommand command) throws IOException {
            String commandString = IPC_COMMAND_MANAGER.saveToString(command).lines().collect(Collectors.joining(" "))
                    + "\n";
            out.write(commandString.getBytes(StandardCharsets.UTF_8));
            out.flush();
        }

        public boolean isClosed() {
            return socket.isClosed();
        }

        @Override
        public void close() throws IOException {
            if (socket != null) {
                socket.close();
            }
        }

        private void runReceiveLoop() {
            try {
                String input;
                while (!socket.isClosed() && (input = in.readLine()) != null) {
                    parseReceivedString(input);
                }
            } catch (IOException e) {
                try {
                    close();
                } catch (IOException e1) {
                    LOGGER.log(Level.WARNING, "Couldn't close socket", e);
                }
            }
        }

        private void parseReceivedString(String input) {
            try {
                IPCCommand cmd = IPC_COMMAND_MANAGER.loadFromString(input);
                if (commandListener != null) {
                    commandListener.accept(cmd);
                }
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Couldn't parse command \"" + input + "\"", e);
            }
        }
    }
}
