package ch.zhaw.ikitomo.settings;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

import ch.zhaw.ikitomo.behavior.NextPositionStrategyFactory;
import ch.zhaw.ikitomo.common.Killable;
import ch.zhaw.ikitomo.common.settings.Settings;
import ch.zhaw.ikitomo.common.tomodachi.TomodachiDefinition;
import ch.zhaw.ikitomo.common.tomodachi.TomodachiEnvironment;
import ch.zhaw.ikitomo.common.tomodachi.TomodachiSettings;
import ch.zhaw.ikitomo.exception.LoadUIException;
import ch.zhaw.ikitomo.settings.model.SettingsModel;
import ch.zhaw.ikitomo.settings.view.BottomNotificationPane;
import ch.zhaw.ikitomo.settings.view.PositiveFloatFilter;
import ch.zhaw.ikitomo.settings.view.TomodachiListViewCell;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;

/**
 * The settings controller
 */
public class SettingsController implements Killable {
    /**
     * The logger
     */
    private static final Logger LOGGER = Logger.getLogger(SettingsController.class.getName());

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
     * The control holding the speed of the tomodachi
     */
    @FXML
    private Spinner<Double> speed;

    /**
     * The spinner for the sleep chance of the tomodachi
     */
    @FXML
    private Spinner<Double> sleepChance;

    /**
     * The spinner for the wake up chance of the tomodachi
     */
    @FXML
    private Spinner<Double> wakeUpChance;

    /**
     * The notification pane to show error messages
     */
    private BottomNotificationPane notificationPane = new BottomNotificationPane();

    /**
     * A property holding the speed of the tomodachi
     */
    private DoubleProperty speedProperty = null;

    /**
     * A property holding the sleep chance of the tomodachi
     */
    private DoubleProperty sleepChanceProperty = null;

    /**
     * A property holding the wake up chance of the tomodachi
     */
    private DoubleProperty wakeUpChanceProperty = null;

    /**
     * The movement combo box
     */
    @FXML
    private ComboBox<NextPositionStrategyFactory> movementComboBox;

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

        List<Spinner<Double>> percentageControls = List.of(sleepChance, wakeUpChance);
        List<Spinner<Double>> doubleSpinnerControls = new ArrayList<>(percentageControls);
        doubleSpinnerControls.add(speed);

        for (Spinner<Double> spinner : percentageControls) {
            spinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 1, 1, 0.01));
        }

        speed.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.01, 5, 1, 0.1));

        for (Spinner<Double> spinner : doubleSpinnerControls) {
            spinner.getEditor().setTextFormatter(PositiveFloatFilter.newFloatTextFormatter());
        }

        speedProperty = DoubleProperty.doubleProperty(speed.getValueFactory().valueProperty());
        sleepChanceProperty = DoubleProperty.doubleProperty(sleepChance.getValueFactory().valueProperty());
        wakeUpChanceProperty = DoubleProperty.doubleProperty(wakeUpChance.getValueFactory().valueProperty());

        // setup movement combo box
        movementComboBox.getItems().addAll(NextPositionStrategyFactory.values());
        movementComboBox.setConverter(new StringConverter<NextPositionStrategyFactory>() {

            @Override
            public String toString(NextPositionStrategyFactory object) {
                return object != null ? object.getName() : "";
            }

            @Override
            public NextPositionStrategyFactory fromString(String string) {
                return NextPositionStrategyFactory.valueOf(string);
            }

        });

        tomodachiList.setItems(model.getTomodachiDefinitions());
        tomodachiList.getSelectionModel().select(model.getEnvironment().getCurrentTomodachiDefinition());

        initProperties(null, settings.getCurrentTomodachiSettings());

        settings.currentTomodachiSettingsBinding()
                .addListener((observable, oldValue, newValue) -> initProperties(oldValue, newValue));

        tomodachiList.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> model.setTomodachi(newValue));

        List<ObservableValue<?>> properties = List.of(speedProperty, sleepChanceProperty, wakeUpChanceProperty,
                movementComboBox.valueProperty());

        for (ObservableValue<?> property : properties) {
            property.addListener((observable, oldValue, newValue) -> model.save());
        }

        rootPane.setBottom(notificationPane);

        model.addSaveExceptionHandler(
                exception -> notificationPane.showError("Couldn't save: " + exception.getMessage()));

        model.addSaveSuccessfullyHandler(() -> notificationPane.showInfo("Saved successfully"));
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
            speedProperty.unbindBidirectional(oldSettings.speedProperty());
            sleepChanceProperty.unbindBidirectional(oldSettings.sleepChanceProperty());
            wakeUpChanceProperty.unbindBidirectional(oldSettings.wakeChanceProperty());
            movementComboBox.valueProperty().unbindBidirectional(oldSettings.nextPositionStrategyFactoryProperty());
        }

        speedProperty.bindBidirectional(newSettings.speedProperty());
        sleepChanceProperty.bindBidirectional(newSettings.sleepChanceProperty());
        wakeUpChanceProperty.bindBidirectional(newSettings.wakeChanceProperty());
        movementComboBox.valueProperty().bindBidirectional(newSettings.nextPositionStrategyFactoryProperty());
    }

    @Override
    public CompletableFuture<Void> kill() {
        model.saveImmediately();
        return CompletableFuture.completedFuture(null);
    }

    /**
     * Checks if Window of SettingsController is displayed on the monitor.
     * 
     * @return If the settings window is visible
     */
    public boolean isVisible() {
        return rootPane.getScene().getWindow().isShowing();
    }

    /**
     * Creates a new settings window based on the settings FXML and returns the
     * Settings controller
     * 
     * @param environment The global environment object
     * @return The new {@link SettingsController}
     */
    public static SettingsController newSettingsUI(TomodachiEnvironment environment) {
        LOGGER.info("Opening settings window");
        URL settingsFxmlUrl = SettingsController.class.getResource("settings.fxml");
        FXMLLoader loader = new FXMLLoader(settingsFxmlUrl);
        try {
            loader.setControllerFactory(param -> {
                if (!param.equals(SettingsController.class)) {
                    throw new LoadUIException("The controller factory can only create SettingsController");
                }
                return new SettingsController(environment);
            });
            Parent parent = loader.load();
            SettingsController controller = loader.getController();
            Scene scene = new Scene(parent);

            Stage stage = new Stage();
            loadIcon(stage);
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

    /**
     * Loads the icon of the settings window
     * 
     * @param stage The stage
     */
    private static void loadIcon(Stage stage) {
        InputStream iconIn = SettingsController.class.getResourceAsStream("/icon.png");
        if (iconIn == null) {
            LOGGER.info("No icon found at  \"/icon.png\"");
            return;
        }
        try (iconIn) {
            stage.getIcons().add(new Image(iconIn));
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Couldn\'t load icon image from \"/icon.png\"", e);
        }

    }

}
