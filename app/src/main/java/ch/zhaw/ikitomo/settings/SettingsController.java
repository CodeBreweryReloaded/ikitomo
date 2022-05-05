package ch.zhaw.ikitomo.settings;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

import ch.zhaw.ikitomo.common.Killable;
import ch.zhaw.ikitomo.common.settings.Settings;
import ch.zhaw.ikitomo.common.tomodachi.TomodachiDefinition;
import ch.zhaw.ikitomo.common.tomodachi.TomodachiEnvironment;
import ch.zhaw.ikitomo.common.tomodachi.TomodachiSettings;
import ch.zhaw.ikitomo.exception.LoadUIException;
import ch.zhaw.ikitomo.settings.view.BottomNotificationPane;
import ch.zhaw.ikitomo.settings.view.FloatFilter;
import ch.zhaw.ikitomo.settings.view.TomodachiListViewCell;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

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
    private static final int SETTINGS_MIN_WIDTH = 650;
    /**
     * The minimum height of the settings window
     */
    private static final int SETTINGS_HEIGHT = 400;

    /**
     * The settings view model
     */
    private SettingsModel model;

    /**
     * The root pane
     */
    @FXML
    private BorderPane rootPane;

    /**
     * The list of available tomodachi files
     */
    @FXML
    private ListView<TomodachiDefinition> tomodachiList;

    /**
     * The text field for the sleep chance of the tomodachi
     */
    @FXML
    private TextField sleepChance;

    /**
     * The text field for the wake up chance of the tomodachi
     */
    @FXML
    private TextField wakeUpChance;

    /**
     * The notification pane to show error messages
     */
    private BottomNotificationPane notificationPane = new BottomNotificationPane();

    /**
     * Private constructor
     * 
     * @param environment The global environment object
     */
    private SettingsController(TomodachiEnvironment environment) {
        this.model = new SettingsModel(environment);
    }

    /**
     * Initializes the controller. This method is called by the FXMLLoader
     */
    @FXML
    private void initialize() {
        Settings settings = model.getSettings();
        tomodachiList.setCellFactory(listView -> new TomodachiListViewCell());
        tomodachiList.setItems(model.getTomodachiDefinitions());
        initProperties(null, settings.getTomodachiSettings());
        settings.currentTomodachiSettingsBinding()
                .addListener((observable, oldValue, newValue) -> initProperties(oldValue, newValue));

        tomodachiList.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> model.setTomodachi(newValue));
        sleepChance.textProperty().addListener((observable, oldValue, newValue) -> model.save());
        wakeUpChance.textProperty().addListener((observable, oldValue, newValue) -> model.save());

        sleepChance.setTextFormatter(FloatFilter.newFloatTextFormatter(FloatFilter.IS_POSITIVE_PREDICATE));
        wakeUpChance.setTextFormatter(FloatFilter.newFloatTextFormatter(FloatFilter.IS_POSITIVE_PREDICATE));

        rootPane.setBottom(notificationPane);
        model.addSaveExceptionHandler(
                exception -> notificationPane.showText("Couldn't save: " + exception.getMessage()));
    }

    /**
     * Initializes the properties of the settings window. If the oldSettings
     * parameter is not null then properties of the text fields are
     * 
     * @param oldSettings
     * @param newSettings
     */
    private void initProperties(TomodachiSettings oldSettings, TomodachiSettings newSettings) {
        if (oldSettings != null) {
            // unbind properties from oldSettings
            sleepChance.textProperty().unbindBidirectional(oldSettings.sleepChanceProperty());
            wakeUpChance.textProperty().unbindBidirectional(oldSettings.wakeChanceProperty());
        }
        sleepChance.textProperty().bindBidirectional(newSettings.sleepChanceProperty(),
                new NumberStringConverter());
        wakeUpChance.textProperty().bindBidirectional(newSettings.wakeChanceProperty(),
                new NumberStringConverter());
    }

    @Override
    public CompletableFuture<Void> kill() {
        model.save();
        return CompletableFuture.completedFuture(null);
    }

    /**
     * Creates a new settings window based on the settings FXML and returns the
     * Settings controller
     * 
     * @param environment The global environment object
     * @return The new {@link SettingsController}
     */
    public static SettingsController newSettingsUI(TomodachiEnvironment environment) {
        URL settingsFxmlUrl = SettingsController.class.getResource("settings.fxml");
        FXMLLoader loader = new FXMLLoader(settingsFxmlUrl);
        try {
            loader.setControllerFactory((param) -> {
                if (!param.equals(SettingsController.class)) {
                    throw new LoadUIException("The controller factory can only create SettingsController");
                }
                return new SettingsController(environment);
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
