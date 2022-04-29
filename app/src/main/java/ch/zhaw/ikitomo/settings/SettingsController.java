package ch.zhaw.ikitomo.settings;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

import ch.zhaw.ikitomo.common.Killable;
import ch.zhaw.ikitomo.common.settings.Settings;
import ch.zhaw.ikitomo.common.tomodachi.TomodachiFile;
import ch.zhaw.ikitomo.exception.LoadUIException;
import ch.zhaw.ikitomo.settings.view.TomodachiListViewCell;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * The settings controller
 */
public class SettingsController implements Killable {

    /**
     * The title of the settings window
     */
    private static final String SETTINGS_TITLE = "Settings";
    /**
     * The minimum width of the settings window
     */
    private static final int SETTINGS_MIN_WIDTH = 500;
    /**
     * The minimum height of the settings window
     */
    private static final int SETTINGS_HEIGHT = 300;

    /**
     * The global settings object
     */
    private Settings settings;

    /**
     * The list of available tomodachi files
     */
    @FXML
    private ListView<TomodachiFile> tomodachiList;

    @FXML
    private TextField sleepChance;

    @FXML
    private TextField wakeUpChance;

    /**
     * Private constructor
     * 
     * @param settings The global settings object
     */
    private SettingsController(Settings settings) {
        this.settings = settings;
    }

    /**
     * Initializes the controller. This method is called by the FXMLLoader
     */
    @FXML
    private void initialize() {
        tomodachiList.setCellFactory(listView -> new TomodachiListViewCell());
        ObservableList<TomodachiFile> tomodachiFiles = settings.getTomodachiFiles();
        tomodachiList.setItems(tomodachiFiles);

    }

    @Override
    public CompletableFuture<Void> kill() {
        return CompletableFuture.completedFuture(null);
    }

    /**
     * Creates a new settings window based on the settings FXML and returns the
     * Settings controller
     * 
     * @param settings The global settings object
     * @return The new {@link SettingsController}
     */
    public static SettingsController newSettingsUI(Settings settings) {
        URL settingsFxmlUrl = SettingsController.class.getResource("settings.fxml");
        FXMLLoader loader = new FXMLLoader(settingsFxmlUrl);
        try {
            loader.setControllerFactory((param) -> {
                if (!param.equals(SettingsController.class)) {
                    throw new LoadUIException("The controller factory can only create SettingsController");
                }
                return new SettingsController(settings);
            });
            Parent parent = loader.load();
            SettingsController controller = loader.getController();
            Scene scene = new Scene(parent);

            Stage stage = new Stage();
            stage.setTitle(SETTINGS_TITLE);
            stage.setScene(scene);
            stage.setMinWidth(SETTINGS_MIN_WIDTH);
            stage.setMinHeight(SETTINGS_HEIGHT);
            stage.show();
            return controller;
        } catch (IOException e) {
            throw new LoadUIException("Could not load settings UI", e);
        }
    }

}
