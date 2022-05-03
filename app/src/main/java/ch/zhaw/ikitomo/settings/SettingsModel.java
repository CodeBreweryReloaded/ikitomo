package ch.zhaw.ikitomo.settings;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import ch.zhaw.ikitomo.common.DelayedRunnable;
import ch.zhaw.ikitomo.common.settings.Settings;
import ch.zhaw.ikitomo.common.settings.SettingsLoader;
import ch.zhaw.ikitomo.common.tomodachi.TomodachiFile;
import javafx.application.Platform;
import javafx.collections.ObservableList;

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

    public SettingsModel(Settings settings) {
        this.settings = settings;
    }

    /**
     * @return the settings
     */
    public Settings getSettings() {
        return settings;
    }

    public void save() {
        delayedSaveRunnable.run();
    }

    private void actuallySaveToFile() {
        try {
            SettingsLoader.saveToDefault(settings);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Could not save settings", e);
            fireSaveExceptionHandler(e);
        }
    }

    public void setTomodachi(TomodachiFile tomodachiFile) {
        settings.setTomodachiModel(tomodachiFile);
        save();
    }

    public void addSaveExceptionHandler(Consumer<Exception> handler) {
        saveExceptionHandler.add(handler);
    }

    private void fireSaveExceptionHandler(Exception e) {
        Platform.runLater(() -> saveExceptionHandler.forEach(handler -> handler.accept(e)));
    }

    public ObservableList<TomodachiFile> getTomodachiFiles() {
        return settings.getTomodachiFiles();
    }

}
