package ch.zhaw.ikitomo;

import ch.zhaw.ikitomo.common.settings.SettingsManager;
import ch.zhaw.ikitomo.common.tomodachi.TomodachiEnvironment;
import ch.zhaw.ikitomo.common.tomodachi.TomodachiManager;
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
        environment = new TomodachiEnvironment(new SettingsManager(), new TomodachiManager());
        environment.load();
        OverlayController overlayController = OverlayController.newOverlayUI(environment, primaryStage);
    }
}
