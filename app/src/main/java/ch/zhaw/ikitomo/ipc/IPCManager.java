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

import ch.zhaw.ikitomo.ipc.IPCCommand.IPCCommandType;

/**
 * A class which manges the inter process communication (IPC)
 */
public class IPCManager implements Closeable {
    /**
     * The logger
     */
    private static final Logger LOGGER = Logger.getLogger(IPCManager.class.getName());
    /**
     * The path to the default well known port file
     */
    private static final String DEFAULT_WELL_KNOWN_PORT_FILE = "./.ipc-port";
    /**
     * The {@link IPCCommandManager} used to serialize and deserialize
     * {@link IPCCommand}s
     */
    private static final IPCCommandManager IPC_COMMAND_MANAGER = new IPCCommandManager();

    /**
     * The path to the well known port file of this {@link IPCManager}
     */
    private Path wellKnownPortFile;

    /**
     * The {@link IPCServer} if started else the ipcServer is null
     */
    private IPCServer ipcServer;

    /**
     * If the {@link IPCCommandType#SHOW_SETTINGS} should be sent to the other
     * instance if detected
     */
    private boolean sendShowSettingsCommand;

    /**
     * If an other instance is running. This flag is set in {@link #init()} and
     * maybe has to wrong value before it was called
     */
    private boolean otherInstanceRunning;

    /**
     * The method to call when the {@link IPCManager} wants to open the settings
     */
    private Runnable showSettingsListener;

    /**
     * A completable future which will complete if it is clear if another instance
     * is running
     */
    private CompletableFuture<Boolean> otherInstanceRunningFuture = new CompletableFuture<>();

    /**
     * Constructor
     * 
     * @param wellKnownPortFile    The path to the well known port file
     * @param showSettingsListener The method to call when the {@link IPCManager}
     *                             wants to open the settings
     */
    IPCManager(Path wellKnownPortFile, Runnable showSettingsListener) {
        this.wellKnownPortFile = Objects.requireNonNull(wellKnownPortFile, "wellKnownPortFile = null");
        this.showSettingsListener = Objects.requireNonNull(showSettingsListener, "showSettingsListener = null");
    }

    /**
     * Sets if the {@link IPCCommandType#SHOW_SETTINGS} should be sent to the other
     * instance (if one is detected)
     * 
     * @param sendShowSettingsCommand If the {@link IPCCommandType#SHOW_SETTINGS} is
     *                                sent
     */
    public void setSendShowSettingsCommand(boolean sendShowSettingsCommand) {
        this.sendShowSettingsCommand = sendShowSettingsCommand;
    }

    /**
     * Gets if {@link IPCCommandType#SHOW_SETTINGS} is sent to the other instance if
     * one is detected
     * 
     * @return If the {@link IPCCommandType#SHOW_SETTINGS} is sent
     */
    public boolean isSendShowSettingsCommand() {
        return sendShowSettingsCommand;
    }

    /**
     * <p>
     * Initializes the {@link IPCManager}. This method checks if the well known port
     * file exists and the {@link IPCManager} can connect to the server. If this is
     * the case, the {@link IPCCommandType#SHOW_SETTINGS} is sent if
     * {@link #sendShowSettingsCommand} is set to true.
     * </p>
     * <p>
     * The {@link #otherInstanceRunning} is set in this method and before is
     * uninitialized.
     * </p>
     */
    public void init() {
        // if the well-known port file exists, we assume that another instance is
        // running
        otherInstanceRunning = Files.exists(wellKnownPortFile);
        if (otherInstanceRunning) {
            LOGGER.log(Level.INFO, "Detected other instance running, trying to contact it");
            tryToConnectToIPCServer();
        }

        // While connecting to the server we might run in to trouble, so we try to start
        // up our own server
        if (!otherInstanceRunning) {
            LOGGER.log(Level.INFO,
                    "The well known port file {0} does not exist or the connection to the server failed, assuming that no other instance is running and starting the ipc server",
                    wellKnownPortFile.toAbsolutePath());
            startUpIPCServer();
        }
        // complete the otherInstanceRunningFuture
        otherInstanceRunningFuture.complete(otherInstanceRunning);

    }

    /**
     * Deletes the well known port file if it exists
     */
    private void deleteWellKnownPortFile() {
        if (Files.exists(wellKnownPortFile)) {
            try {
                LOGGER.log(Level.INFO, "Deleting well known port file {0}", wellKnownPortFile.toAbsolutePath());
                Files.delete(wellKnownPortFile);
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Could not delete well known port file", e);
            }
        }
    }

    /**
     * Trying to connect to the ipc server of the other instance. If it fails,
     * {@link #otherInstanceRunning} is set to false
     */
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

    /**
     * Sends the {@link IPCCommandType#SHOW_SETTINGS} to the other instance if
     * {@link #sendShowSettingsCommand} is set to true
     * 
     * @param client The client
     * @throws IOException When something goes wrong while sending the command
     */
    private void onClientConnected(IPCClient client) throws IOException {
        if (isSendShowSettingsCommand()) {
            LOGGER.log(Level.INFO, "Sending show settings command");
            client.send(IPCCommand.newShowSettingsCommand());
        }
    }

    /**
     * Starts the {@link IPCServer} and writing the port to the well known port file
     */
    private void startUpIPCServer() {
        if (ipcServer != null && !ipcServer.isClosed()) {
            return;
        }
        try {
            ipcServer = new IPCServer();
            ipcServer.setCommandListener(this::onCommandReceived);
            ipcServer.start();

            LOGGER.log(Level.INFO, "Starting up ipc server on port \"{0}\"", ipcServer.getPort());
            writeWellKnownPortFile(ipcServer.getPort());
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                this.deleteWellKnownPortFile();
                this.closeIPCServer();
            }));
        } catch (IOException e) {
            LOGGER.log(Level.WARNING,
                    "Couldn't start IPC server or write well-known port file at \"" + wellKnownPortFile + "\"", e);
            closeIPCServer();
            otherInstanceRunning = true; // maybe another instance wrote the well known port file before we could -> try
                                         // to connect to it
        }
    }

    /**
     * A listener which is called by the {@link #ipcServer} when a command is
     * received
     * 
     * @param command The received command
     */
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

    /**
     * Closes the {@link #ipcServer} if it is open else the method fails silently
     */
    private void closeIPCServer() {
        if (ipcServer != null && !ipcServer.isClosed()) {
            try {
                ipcServer.close();
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Couldn't stop ipc server", e);
            }
        }
    }

    /**
     * Gets a future which complete when this manager determined if another instance
     * is running. This is done in {@link #init()}. The future returns true if
     * another instance was detected and false if not.
     * 
     * @return The future which completes to whether another instance is running
     */
    public CompletableFuture<Boolean> isOtherInstanceRunning() {
        return otherInstanceRunningFuture;
    }

    /**
     * Writes the well known port file with the given port
     * 
     * @param port The port to write
     * @throws IOException If something went wrong wile writing
     */
    private void writeWellKnownPortFile(int port) throws IOException {
        var wellKnownPortContent = String.valueOf(port).getBytes(StandardCharsets.UTF_8);
        Files.write(wellKnownPortFile, wellKnownPortContent, StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.SYNC);
    }

    /**
     * Reads the well known port file from {@link #wellKnownPortFile} and returns
     * the port written in it
     * 
     * @return The port read from {@link #wellKnownPortFile}
     * @throws IOException If something went wrong while reading
     */
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
        if (ipcServer != null) {
            ipcServer.close();
        }
    }

    /**
     * The static instance of {@link IPCManager}
     */
    private static IPCManager instance;

    /**
     * 
     * Initializes the global {@link IPCManager} instance. If the instance already
     * exists this method will return instantly, ignoring the parameters.
     * 
     * @param sendShowSettingsCommand If {@link IPCCommandType#SHOW_SETTINGS} should
     *                                be sent to the other instance when one is
     *                                detected
     * @param showSettingsListener    The method called by the {@link IPCServer}
     *                                when {@link IPCCommandType#SHOW_SETTINGS} is
     *                                received
     * @return The created {@link IPCManager}
     * @see #getInstance()
     */
    public static IPCManager createInstance(boolean sendShowSettingsCommand, Runnable showSettingsListener) {
        if (instance != null) {
            LOGGER.warning("IPCManager already created");
            return instance;
        }
        Path wellKnownPortFile = Paths.get(DEFAULT_WELL_KNOWN_PORT_FILE);
        instance = new IPCManager(wellKnownPortFile, showSettingsListener);
        instance.setSendShowSettingsCommand(sendShowSettingsCommand);
        return instance;
    }

    /**
     * Returns the global {@link IPCManager} instance. To initialize the global
     * instance, use {@link #createInstance(boolean, Runnable)}. If the instance
     * doesn't exist a {@link IllegalStateException} is thrown.
     * 
     * @return The global instance
     * @throws IllegalStateException If the instance wasn't initialized with
     *                               {@link #createInstance(boolean, Runnable)}
     * @see #createInstance(boolean, Runnable)
     */
    public static IPCManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("IPCManager not initialized");
        }
        return instance;
    }

    /**
     * The IPC server class
     */
    static class IPCServer implements Closeable {
        /**
         * Logger
         */
        private static final Logger LOGGER = Logger.getLogger(IPCServer.class.getName());
        /**
         * The server socket
         */
        private ServerSocket server;
        /**
         * The thread of the server
         */
        private Thread thread = new Thread(this::runThread, "IPC Server Thread");

        /**
         * The lock for {@link #clients}
         */
        private Object clientsLock = new Object();

        /**
         * A list of all connected clients
         */
        private List<IPCClient> clients = new ArrayList<>();

        /**
         * A listener which is called when an {@link IPCCommand} was received
         */
        private Consumer<IPCCommand> commandListener;
        /**
         * A listener which is called when a client connected to this server
         */
        private Consumer<IPCClient> connectListener;

        /**
         * Constructor
         * 
         * @ses {@link #start()}
         */
        public IPCServer() {
        }

        /**
         * Starts the ipc server on a random port on the loopback interface
         * 
         * @see InetAddress#getLoopbackAddress()
         * @see #getPort()
         */
        public void start() throws IOException {
            server = new ServerSocket(0, 50, InetAddress.getLoopbackAddress());
            thread.setDaemon(true);
            thread.start();
        }

        /**
         * The main loop of the server called by {@link #thread}
         */
        private void runThread() {
            try {
                while (!server.isClosed()) {
                    Socket socket = server.accept();
                    LOGGER.log(Level.INFO, "Accepted connection from {0}", socket.getInetAddress());
                    var client = new IPCClient(socket, commandListener, this::onClientCloses);
                    synchronized (clientsLock) {
                        clients.add(client);
                    }
                    if (connectListener != null) {
                        connectListener.accept(client);
                    }
                }
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Couldn't accept connection", e);
            }
        }

        private void onClientCloses(IPCClient client) {
            synchronized (clientsLock) {
                clients.remove(client);
            }
        }

        /**
         * 
         * Sets a listener which is called when an {@link IPCCommand} was received
         * 
         * @param commandListener The listener
         */
        public void setCommandListener(Consumer<IPCCommand> commandListener) {
            this.commandListener = commandListener;
        }

        /**
         * Sets a listener which is called when a client connected to this server
         * 
         * @param connectListener The listener
         */
        public void setConnectListener(Consumer<IPCClient> connectListener) {
            this.connectListener = connectListener;
        }

        /**
         * Gets the port of the server
         * 
         * @return The port
         */
        public int getPort() {
            return server.getLocalPort();
        }

        /**
         * Returns if the server is closed
         * 
         * @return if it is closed
         */
        public boolean isClosed() {
            return server.isClosed();
        }

        @Override
        public void close() throws IOException {
            synchronized (clientsLock) {
                var clientListCopy = new ArrayList<>(clients);
                for (IPCClient client : clientListCopy) {
                    try {
                        client.close();
                    } catch (IOException e) {
                        LOGGER.log(Level.WARNING, "Couldn't close a client", e);
                    }
                }
            }
            if (server != null) {
                server.close();
            }
        }
    }

    /**
     * A client of a server either created by the server instance to represent a
     * user or by the {@link IPCManager} to connect a server
     */
    static class IPCClient implements Closeable {
        /**
         * The logger
         */
        private static final Logger LOGGER = Logger.getLogger(IPCClient.class.getName());
        /**
         * The socket
         */
        private Socket socket;
        /**
         * The buffered reader to read from the socket
         */
        private BufferedReader in;
        /**
         * The output stream to write to the socket
         */
        private OutputStream out;
        /**
         * A listener which is called when the client receives a command
         */
        private Consumer<IPCCommand> commandListener;

        /**
         * A listener which is called when the client is closed
         */
        private Consumer<IPCClient> closeListener;

        /**
         * Constructor
         * <p>
         * Creates a new socket on the given port and the loop back interface. The
         * commandListener should be set here as it might miss commands when it is set
         * later.
         * </p>
         * 
         * @param port            The port to connect to
         * @param commandListener The command listener
         * @throws IOException If the socket couldn't be created
         * @see InetAddress#getLoopbackAddress()
         */
        public IPCClient(int port, Consumer<IPCCommand> commandListener) throws IOException {
            this(new Socket(InetAddress.getLoopbackAddress(), port), commandListener, null);
        }

        /**
         * Constructor
         * 
         * @param socket          The socket to use
         * @param commandListener The command listener
         * @throws IOException If the socket can't be setup
         */
        public IPCClient(Socket socket, Consumer<IPCCommand> commandListener, Consumer<IPCClient> closeListener)
                throws IOException {
            this.commandListener = commandListener;
            this.closeListener = closeListener;
            setupSocket(socket);
        }

        /**
         * Sets up the given socket
         * 
         * @param socket The socket
         * @throws IOException If something went wrong while setting up
         */
        private void setupSocket(Socket socket) throws IOException {
            this.socket = socket;
            this.socket.setKeepAlive(true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = socket.getOutputStream();
            Thread t = new Thread(this::runReceiveLoop, "ICP Client Thread");
            t.setDaemon(true);
            t.start();
        }

        /**
         * Sends the given IPC command to the server/client
         * 
         * @param command The command
         * @throws IOException If something went wrong while sending
         */
        public void send(IPCCommand command) throws IOException {
            String commandString = IPC_COMMAND_MANAGER.saveToString(command).lines().collect(Collectors.joining(" "))
                    + "\n";
            out.write(commandString.getBytes(StandardCharsets.UTF_8));
            out.flush();
        }

        /**
         * Gets if this client is closed
         * 
         * @return If the client is closed
         */
        public boolean isClosed() {
            return socket.isClosed();
        }

        @Override
        public void close() throws IOException {
            if (closeListener != null) {
                closeListener.accept(this);
            }
            if (socket != null) {
                socket.close();
            }
        }

        /**
         * The run method of the thread
         */
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

        /**
         * The parses the given string and calls the command listener if the received
         * command was valid and the listener isn't null
         * 
         * @param input The input to parse
         */
        private void parseReceivedString(String input) {
            try {
                IPCCommand cmd = IPC_COMMAND_MANAGER.loadFromString(input);
                LOGGER.log(Level.INFO, "Received command: {0}", cmd);
                if (commandListener != null) {
                    commandListener.accept(cmd);
                }
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Couldn't parse command \"" + input + "\"", e);
            }
        }
    }
}
