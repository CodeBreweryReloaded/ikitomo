package ch.zhaw.ikitomo.ipc;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.function.Consumer;

public class IPCManager {
    private static final String DEFAULT_WELL_KNOWN_PORT_FILE = "./.ipc-port";
    private Path wellKnownPortFile;

    private Consumer<IPCClient> ipcClientConnectedListener;

    private IPCManager(Path wellKnownPortFile) {
        this.wellKnownPortFile = wellKnownPortFile;
    }

    public void init() {
        if (Files.exists(wellKnownPortFile)) {
        } else {
            startUpIPCServer();
        }
    }

    private void tryToConnectToIPCServer() {

    }

    private void startUpIPCServer() {
    }

    public Path getWellKnownPortFile() {
        return wellKnownPortFile;
    }

    private static IPCManager instance;

    public static IPCManager getInstance() {
        if (instance == null) {
            Path wellKnownPortFile = Paths.get(DEFAULT_WELL_KNOWN_PORT_FILE);
            instance = new IPCManager(wellKnownPortFile);
        }
        return instance;
    }

    private static class IPCServer implements Closeable {
        private int port;
        private Path wellKnownPortFile;
        private ServerSocket server;
        private Thread thread = new Thread(this::runThread, "IPC Server Thread");

        public IPCServer(int port, Path wellKnownPortFile) {
            this.port = port;
            this.wellKnownPortFile = wellKnownPortFile;
        }

        public void start() throws IOException {
            var wellKnownPortContent = String.valueOf(port).getBytes(StandardCharsets.UTF_8);
            Files.write(wellKnownPortFile, wellKnownPortContent, StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.SYNC);
            server = new ServerSocket(port, 0, InetAddress.getLoopbackAddress());
            thread.start();
        }

        private void runThread() {
            while (!server.isClosed()) {
                // Socket socket = server.accept();
            }
        }

        @Override
        public void close() throws IOException {
            if (server != null) {
                server.close();
            }
        }
    }

    public static class IPCClient implements Closeable {

        @Override
        public void close() throws IOException {
            // TODO Auto-generated method stub

        }
    }
}
