package ch.zhaw.ikitomo;

import ch.zhaw.ikitomo.common.settings.Settings;
import ch.zhaw.ikitomo.common.settings.SettingsLoader;
import ch.zhaw.ikitomo.overlay.OverlayController;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * The javafx application class to bootstrap the GUI.
 */
public class IkitomoApplication extends Application {
    /**
     * The global setings
     */
    private Settings settings;

    @Override
    public void start(Stage primaryStage) throws Exception {
        //settings = SettingsLoader.load(SettingsLoader.DEFAULT_SETTINGS_PATH);
        //settings.loadPossibleTomodachiFiles();
        OverlayController overlayController = OverlayController.newOverlayUI(settings, primaryStage);
    }
}
