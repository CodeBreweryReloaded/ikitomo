package ch.zhaw.ikitomo;

import ch.zhaw.ikitomo.common.settings.Settings;
import ch.zhaw.ikitomo.common.settings.SettingsManager;
import ch.zhaw.ikitomo.settings.SettingsController;
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
        settings = SettingsManager.loadFromDefault();
        settings.loadPossibleTomodachiFiles();
        SettingsController.newSettingsUI(settings);
    }

}
