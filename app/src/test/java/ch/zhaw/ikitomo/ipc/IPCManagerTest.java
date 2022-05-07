package ch.zhaw.ikitomo.ipc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;

import ch.zhaw.ikitomo.ipc.IPCManager.IPCClient;
import ch.zhaw.ikitomo.ipc.IPCManager.IPCServer;

class IPCManagerTest {

    @Test
    void testPortFinding() throws IOException {
        try (IPCServer server1 = new IPCServer();
                IPCServer server2 = new IPCServer()) {
            server1.start();
            server2.start();
            assertTrue(server1.getPort() != server2.getPort(), "Ports must be different");
        }
    }

    @Test
    void testIPCServerReceiving() throws IOException, InterruptedException {
        var latch = new CountDownLatch(1);
        var command = IPCCommand.newShowSettingsCommand();
        try (IPCServer server = new IPCServer()) {
            server.setCommandListener(createCommandReceiverListener(latch, command));
            server.start();
            try (IPCClient client = new IPCClient(server.getPort(), null)) {
                client.send(command);
            }
            assertTrue(latch.await(2, TimeUnit.SECONDS), "Command not received");
        }
    }

    @Test
    void testIPCClientReceiving() throws IOException, InterruptedException {
        var latch = new CountDownLatch(1);
        var command = IPCCommand.newShowSettingsCommand();
        try (IPCServer server = new IPCServer()) {
            server.setConnectListener(createConnectListener(command));
            server.start();
            try (IPCClient client = new IPCClient(server.getPort(), createCommandReceiverListener(latch, command))) {
                assertTrue(latch.await(2, TimeUnit.SECONDS), "Command not received");
            }
        }
    }

    private Consumer<IPCCommand> createCommandReceiverListener(CountDownLatch latch, IPCCommand command) {
        return cmd -> {
            assertEquals(command, cmd);
            latch.countDown();
        };
    }

    private Consumer<IPCClient> createConnectListener(IPCCommand command) {
        return client -> {
            try {
                client.send(command);
            } catch (IOException e) {
                throw new RuntimeException("Couldn't not send command", e);
            }
        };
    }

    @Test
    void testWellKnownPortFileIsCreated() throws IOException {
        Runnable identityRunnable = () -> {
        };
        Path portFile = Path.of("./testWellKnownPortFileIsInvalid.txt");
        try (var manager1 = new IPCManager(portFile, identityRunnable)) {
            manager1.init();
            assertFalse(manager1.isOtherInstanceRunning());
        } finally {
            Files.deleteIfExists(portFile);
        }
    }

    @Test
    void testOpenSettings() throws InterruptedException, IOException {
        Runnable identityRunnable = () -> {
        };
        var latch = new CountDownLatch(1);
        Path portFile = Files.createTempFile("ipc-manager-test", "");
        try (var manager1 = new IPCManager(portFile, latch::countDown);
                var manager2 = new IPCManager(portFile, identityRunnable)) {
            manager1.init();
            manager2.setSendShowSettingsCommand(true);
            manager2.init();
            assertTrue(manager2.isOtherInstanceRunning());
            assertFalse(manager1.isOtherInstanceRunning());
            assertTrue(latch.await(2, TimeUnit.SECONDS), "Command not received");
        }
    }

    @Test
    void testNotOpeningSettings() throws IOException, InterruptedException {
        Runnable identityRunnable = () -> {
        };
        var latch = new CountDownLatch(1);
        Path portFile = Files.createTempFile("ipc-manager-test", "");
        try (var manager1 = new IPCManager(portFile, latch::countDown);
                var manager2 = new IPCManager(portFile, identityRunnable)) {
            manager1.init();
            manager2.setSendShowSettingsCommand(false);
            manager2.init();
            assertTrue(manager2.isOtherInstanceRunning());
            assertFalse(manager1.isOtherInstanceRunning());
            assertFalse(latch.await(2, TimeUnit.SECONDS), "Command not received");
        }
    }

}
