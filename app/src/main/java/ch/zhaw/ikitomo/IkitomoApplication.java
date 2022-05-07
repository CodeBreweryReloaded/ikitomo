package ch.zhaw.ikitomo;

import java.util.List;

import ch.zhaw.ikitomo.common.settings.SettingsManager;
import ch.zhaw.ikitomo.common.tomodachi.TomodachiEnvironment;
import ch.zhaw.ikitomo.common.tomodachi.TomodachiManager;
import ch.zhaw.ikitomo.ipc.IPCManager;
import ch.zhaw.ikitomo.overlay.OverlayController;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * The javafx application class to bootstrap the GUI.
 */
public class IkitomoApplication extends Application {
    /**
     * The environment of the application.
     */
    private TomodachiEnvironment environment;

    @Override
    public void start(Stage primaryStage) {
        IkitomoParameters parameters = IkitomoParameters.parseParameters(getParameters());

        IPCManager.init(parameters.showSettings(), this::showSettings);

        environment = new TomodachiEnvironment(new SettingsManager(), new TomodachiManager());
        OverlayController overlayController = OverlayController.newOverlayUI(environment, primaryStage);
    }

    private void showSettings() {

    }

    private static record IkitomoParameters(boolean showSettings, boolean showHelp) {
        public static IkitomoParameters parseParameters(Parameters parameters) {
            List<String> unnamedParams = parameters.getUnnamed();
            return new IkitomoParameters(unnamedParams.contains("--show-settings"),
                    unnamedParams.contains("--help"));
        }
    }
}
