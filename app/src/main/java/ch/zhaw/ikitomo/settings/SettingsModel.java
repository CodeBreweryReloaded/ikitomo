package ch.zhaw.ikitomo.settings;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import ch.zhaw.ikitomo.common.DelayedRunnable;
import ch.zhaw.ikitomo.common.settings.Settings;
import ch.zhaw.ikitomo.common.settings.SettingsManager;
import ch.zhaw.ikitomo.common.tomodachi.TomodachiDefinition;
import javafx.application.Platform;
import javafx.collections.ObservableList;

/**
 * The model class for the {@link SettingsController}
 */
public class SettingsModel {
    /**
     * the logger
     */
    private static final Logger logger = Logger.getLogger(SettingsModel.class.getName());
    /**
     * the delay in ms between the last keystroke and the saving of the settings
     */
    private static final long SAVING_DELAY = 200;

    /**
     * The global settings object
     */
    private Settings settings;

    /**
     * A delayed runnable for saving the settings. It prevents the save function
     * being called too rapidly
     */
    private DelayedRunnable delayedSaveRunnable = new DelayedRunnable(this::actuallySaveToFile,
            SAVING_DELAY);

    /**
     * A List with exception handlers called when saving the settings fails
     */
    private List<Consumer<Exception>> saveExceptionHandler = new ArrayList<>();

    /**
     * Constructor
     * 
     * @param settings The global {@link Settings} object
     */
    public SettingsModel(Settings settings) {
        this.settings = settings;
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
     * Saves the current settings after a period to make sure that the program only
     * saves when the user finished an action.
     * The waiting period is defined in {@link #SAVING_DELAY}
     */
    public void save() {
        delayedSaveRunnable.run();
    }

    /**
     * The method called by the {@link #delayedSaveRunnable} which actually saves
     * the content of settings
     */
    private void actuallySaveToFile() {
        try {
            SettingsManager.saveToDefault(settings);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Could not save settings", e);
            fireSaveExceptionHandler(e);
        }
    }

    /**
     * Setts the current {@link TomodachiDefinition}
     * 
     * @param tomodachiFile the tomodachi file
     */
    public void setTomodachi(TomodachiDefinition tomodachiFile) {
        settings.setTomodachiModel(tomodachiFile);
        save();
    }

    /**
     * Adds an exception handler which is called when saving the settings fails
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
     * @return the list
     */
    public ObservableList<TomodachiDefinition> getTomodachiDefinitions() {
        return settings.getTomodachiFiles();
    }

}
