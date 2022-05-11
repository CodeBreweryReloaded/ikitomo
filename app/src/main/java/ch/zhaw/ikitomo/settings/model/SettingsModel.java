package ch.zhaw.ikitomo.settings.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import ch.zhaw.ikitomo.common.DelayedRunnable;
import ch.zhaw.ikitomo.common.settings.Settings;
import ch.zhaw.ikitomo.common.tomodachi.TomodachiDefinition;
import ch.zhaw.ikitomo.common.tomodachi.TomodachiEnvironment;
import ch.zhaw.ikitomo.common.tomodachi.TomodachiSettings;
import ch.zhaw.ikitomo.settings.SettingsController;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.collections.ObservableList;

/**
 * The model class for the {@link SettingsController}
 */
public class SettingsModel {
    /**
     * The logger
     */
    private static final Logger LOGGER = Logger.getLogger(SettingsModel.class.getName());

    /**
     * The delay in ms between the last keystroke and the saving of the settings
     */
    private static final long SAVING_DELAY = 200;

    /**
     * The global tomodachi environment object
     */
    private TomodachiEnvironment environment;

    /**
     * The global settings object
     */
    private Settings settings;

    private ObjectBinding<TomodachiSettings> currentTomodachiSettings;

    /**
     * A delayed runnable for saving the settings. It prevents the save function
     * being called too rapidly
     */
    private DelayedRunnable delayedSaveRunnable = new DelayedRunnable(this::saveImmediately,
            SAVING_DELAY);

    /**
     * A List with exception handlers called when saving the settings fails
     */
    private List<Consumer<Exception>> saveExceptionHandler = new ArrayList<>();

    /**
     * Constructor
     * 
     * @param environment the global environnement object
     */
    public SettingsModel(TomodachiEnvironment environment) {
        this.environment = environment;
        this.settings = environment.getSettings();
        this.currentTomodachiSettings = Bindings.createObjectBinding(this::calculateCurrentTomodachiSettings,
                settings.tomodachiIDProperty());
    }

    private TomodachiSettings calculateCurrentTomodachiSettings() {
        String currentID = settings.getTomodachiID();
        if (settings.containsTomodachiSettings(currentID)) {
            TomodachiDefinition definition = environment.getTomodachiDefinition(currentID);
            if (definition == null) {
                throw new IllegalStateException("Unknown tomodachi ID: \"%s\"".formatted(currentID));
            }
            settings.setTomodachiSettings(currentID, definition.getSettings());

        }
        return settings.getTomodachiSettings(currentID);
    }

    /**
     * Gets the binding to the current tomodachi settings. If the currently selected
     * tomodachi id doesn't have a settings associated with it, then the default
     * settings from the {@link TomodachiDefinition} is copied over and set in
     * {@link #settings}
     * 
     * @return The tomodachi settings of the currently selected tomodachi
     */
    public ObjectBinding<TomodachiSettings> currentTomodachiSettings() {
        return currentTomodachiSettings;
    }

    /**
     * @return the currentTomodachiSettings
     */
    public TomodachiSettings getCurrentTomodachiSettings() {
        return currentTomodachiSettings.get();
    }

    /**
     * Gets the settings object
     * 
     * @return The settings
     */
    public Settings getSettings() {
        return settings;
    }

    /**
     * Gets the environment object
     * 
     * @return The environment object
     */
    public TomodachiEnvironment getEnvironment() {
        return environment;
    }

    /**
     * Saves the current settings after a period to make sure that the program only
     * saves when the user finished an action.
     * The waiting period is defined in {@link #SAVING_DELAY}
     */
    public void save() {
        delayedSaveRunnable.run();
    }

    /**
     * Saves the settings immediately with no synchronization. {@link #save()}
     * may be used normally since it only saves after the {@link #save()} method
     * isn't invoked for a period
     * 
     * @see #save()
     */
    public void saveImmediately() {
        try {
            environment.save();
            LOGGER.info("Saved settings");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Could not save settings", e);
            fireSaveExceptionHandler(e);
        }
    }

    /**
     * Sets the current {@link TomodachiDefinition}
     * 
     * @param tomodachiFile the tomodachi file
     */
    public void setTomodachi(TomodachiDefinition tomodachiFile) {
        settings.setTomodachiID(tomodachiFile.getID());
        save();
    }

    /**
     * Adds an exception handler which is called when saving the settings fail
     * 
     * @param handler The handler
     */
    public void addSaveExceptionHandler(Consumer<Exception> handler) {
        saveExceptionHandler.add(handler);
    }

    /**
     * Fires all {@link #saveExceptionHandler}
     * 
     * @param e The exception which is given to the exception handlers
     */
    private void fireSaveExceptionHandler(Exception e) {
        Platform.runLater(() -> saveExceptionHandler.forEach(handler -> handler.accept(e)));
    }

    /**
     * Gets a list with all tomodachi files
     * 
     * @return The list
     */
    public ObservableList<TomodachiDefinition> getTomodachiDefinitions() {
        return environment.getTomodachiDefinitions();
    }

}
