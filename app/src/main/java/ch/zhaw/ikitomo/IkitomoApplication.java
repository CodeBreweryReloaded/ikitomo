package ch.zhaw.ikitomo;

import ch.zhaw.ikitomo.common.settings.Settings;
import ch.zhaw.ikitomo.common.settings.SettingsLoader;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * The javafx application class to bootstrap the gui
 */
public class IkitomoApplication extends Application {
    /**
     * the global setings
     */
    private Settings settings;

    @Override
    public void start(Stage primaryStage) throws Exception {
        settings = SettingsLoader.load(SettingsLoader.DEFAULT_SETTINGS_PATH);
        settings.loadPossibleTomodachiFiles();
    }

}
