package ch.zhaw.ikitomo.ipc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;

import ch.zhaw.ikitomo.ipc.IPCCommand.IPCCommandType;
import ch.zhaw.ikitomo.ipc.IPCManager.IPCClient;
import ch.zhaw.ikitomo.ipc.IPCManager.IPCServer;

/**
 * Tests the {@link IPCManager}
 */
class IPCManagerTest {

    /**
     * Tests if the server uses an unallocated port
     */
    @Test
    void testPortFinding() throws IOException {
        try (IPCServer server1 = new IPCServer();
                IPCServer server2 = new IPCServer()) {
            server1.start();
            server2.start();
            assertTrue(server1.getPort() != server2.getPort(), "Ports must be different");
        }
    }

    /**
     * Tests if the server receives commands from clients
     */
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

    /**
     * Tests if the client receives commands from the server
     * 
     * @throws IOException          If the server/client couldn't be created
     * @throws InterruptedException The test was interrupted
     */
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

    /**
     * A helper method which creates a command listener which will countdown the
     * given latch and check that the received command is the same as the given
     * command
     * 
     * @param latch   The latch to countdown
     * @param command The command which has to be received
     * @return The created listener
     */
    private Consumer<IPCCommand> createCommandReceiverListener(CountDownLatch latch, IPCCommand command) {
        return cmd -> {
            assertEquals(command, cmd);
            latch.countDown();
        };
    }

    /**
     * A helper method which creates a connection listener which sends the given
     * command
     * 
     * @param command The command to send when a client connects
     * @return The connection listener
     */
    private Consumer<IPCClient> createConnectListener(IPCCommand command) {
        return client -> {
            try {
                client.send(command);
            } catch (IOException e) {
                throw new RuntimeException("Couldn't not send command", e);
            }
        };
    }

    /**
     * Tests if the well known port file is created
     */
    @Test
    void testWellKnownPortFileIsCreated()
            throws IOException, InterruptedException, ExecutionException, TimeoutException {
        Runnable identityRunnable = () -> {
        };
        Path portFile = Path.of("./testWellKnownPortFileIsInvalid.txt");
        try (var manager1 = new IPCManager(portFile, identityRunnable)) {
            manager1.init();
            assertFalse(manager1.isOtherInstanceRunning().get(2, TimeUnit.SECONDS));
        } finally {
            Files.deleteIfExists(portFile);
        }
    }

    /**
     * Tests if the settings open listener is called when an other instance is
     * launched and {@link IPCManager#setSendShowSettingsCommand(boolean)} is set to
     * true
     */
    @Test
    void testOpenSettings() throws InterruptedException, IOException, ExecutionException, TimeoutException {
        Runnable identityRunnable = () -> {
        };
        var latch = new CountDownLatch(1);
        Path portFile = Files.createTempFile("ipc-manager-test", "");
        try (var manager1 = new IPCManager(portFile, latch::countDown);
                var manager2 = new IPCManager(portFile, identityRunnable)) {
            manager1.init();
            manager2.setSendShowSettingsCommand(true);
            manager2.init();
            assertTrue(manager2.isOtherInstanceRunning().get(2, TimeUnit.SECONDS));
            assertFalse(manager1.isOtherInstanceRunning().get(2, TimeUnit.SECONDS));
            assertTrue(latch.await(2, TimeUnit.SECONDS), "Command not received");
        }
    }

    /**
     * Tests if {@link IPCManager#setSendShowSettingsCommand(boolean)} is set to
     * false then the {@link IPCCommandType#SHOW_SETTINGS} should be sent
     */
    @Test
    void testNotOpeningSettings() throws IOException, InterruptedException, ExecutionException, TimeoutException {
        Runnable identityRunnable = () -> {
        };
        var latch = new CountDownLatch(1);
        Path portFile = Files.createTempFile("ipc-manager-test", "");
        try (var manager1 = new IPCManager(portFile, latch::countDown);
                var manager2 = new IPCManager(portFile, identityRunnable)) {
            manager1.init();
            manager2.setSendShowSettingsCommand(false);
            manager2.init();
            assertTrue(manager2.isOtherInstanceRunning().get(2, TimeUnit.SECONDS));
            assertFalse(manager1.isOtherInstanceRunning().get(2, TimeUnit.SECONDS));
            assertFalse(latch.await(2, TimeUnit.SECONDS), "Command not received");
        }
    }

}
