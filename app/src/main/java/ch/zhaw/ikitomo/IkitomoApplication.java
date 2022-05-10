package ch.zhaw.ikitomo;

import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

import ch.zhaw.ikitomo.common.settings.SettingsManager;
import ch.zhaw.ikitomo.common.tomodachi.TomodachiEnvironment;
import ch.zhaw.ikitomo.common.tomodachi.TomodachiManager;
import ch.zhaw.ikitomo.ipc.IPCManager;
import ch.zhaw.ikitomo.overlay.OverlayController;
import ch.zhaw.ikitomo.trayicon.TrayIconController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

/**
 * The javafx application class to bootstrap the GUI.
 */
public class IkitomoApplication extends Application {

    /**
     * {@link Logger} of the application.
     */
    private static final Logger LOGGER = Logger.getLogger(IkitomoApplication.class.getName());

    /**
     * The environment of the application.
     */
    private TomodachiEnvironment environment;

    /**
     * Instance of {@link OverlayController}
     */
    private OverlayController overlayController;

    /**
     * Instance of {@link TrayIconController}
     */
    private TrayIconController trayIconController;

    @Override
    public void start(Stage primaryStage) {
        IkitomoParameters parameters = IkitomoParameters.parseParameters(getParameters());

        IPCManager ipcManager = IPCManager.createInstance(parameters.showSettings(), () -> {
            if (trayIconController != null) {
                trayIconController.showSettings();
            }
        });
        ipcManager.init();

        try {
            Boolean otherInstanceRunning = ipcManager.isOtherInstanceRunning().get(200, TimeUnit.SECONDS);
            if (Boolean.TRUE.equals(otherInstanceRunning)) {
                LOGGER.log(Level.INFO, "Other instance running, exiting");
                Platform.exit();
                return;
            }
        } catch (ExecutionException | TimeoutException e) {
            LOGGER.log(Level.SEVERE, "Could not check if other instance is running", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        environment = new TomodachiEnvironment(new SettingsManager(), new TomodachiManager());
        overlayController = OverlayController.newOverlayUI(environment, primaryStage);
        trayIconController = TrayIconController.newOverlayUI(this);
    }

    /**
     * Closes the application
     */
    public void close() {
        try {
            overlayController.kill().join();
        } catch (CompletionException | CancellationException e) {
            LOGGER.log(Level.WARNING, "Couldn't kill " + OverlayController.class.getName(), e);
        }
        try {
            trayIconController.kill().join();
        } catch (CompletionException | CancellationException e) {
            LOGGER.log(Level.WARNING, "Couldn't kill " + TrayIconController.class.getName(), e);
        }

        Platform.exit();
    }

    /**
     * Returns the environment of the application
     *
     * @return environment the global environment
     */
    public TomodachiEnvironment getEnvironment() {
        return environment;
    }

    private static record IkitomoParameters(boolean showSettings, boolean showHelp) {
        public static IkitomoParameters parseParameters(Parameters parameters) {
            List<String> unnamedParams = parameters.getUnnamed();
            return new IkitomoParameters(unnamedParams.contains("--show-settings"),
                    unnamedParams.contains("--help"));
        }
    }
}
