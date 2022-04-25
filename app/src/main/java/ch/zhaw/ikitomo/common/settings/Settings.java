package ch.zhaw.ikitomo.common.settings;

import ch.zhaw.ikitomo.common.tomodachi.TomodachiFile;
import ch.zhaw.ikitomo.common.tomodachi.TomodachiSettings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Represents the settings of the application.
 */
public class Settings {
    /**
     * The possible tomodachi files
     */
    private ObservableList<TomodachiFile> tomodachiFiles = FXCollections.observableArrayList();
    /**
     * The currently seleted tomodachi model
     */
    private ObjectProperty<TomodachiFile> tomodachiFile = new SimpleObjectProperty<>();
    /**
     * The current settings of the tomodachi.
     */
    private TomodachiSettings tomodachiSettings;

    /**
     * Constructor.
     */
    public Settings() {
        this.tomodachiSettings = new TomodachiSettings();
    }

    /**
     * Constructor
     * 
     * @param tomodachiID       The current id
     * @param tomodachiSettings The tomodachi settings
     */
    public Settings(TomodachiFile tomodachiFile, TomodachiSettings tomodachiSettings) {
        this.tomodachiFile.set(tomodachiFile);
        this.tomodachiSettings = tomodachiSettings;
    }

    /**
     * @return The tomodachiFiles
     */
    public ObservableList<TomodachiFile> getTomodachiFiles() {
        return tomodachiFiles;
    }

    /**
     * @return The tomodachi ID property
     */
    public ObjectProperty<TomodachiFile> tomodachiFileProperty() {
        return tomodachiFile;
    }

    /**
     * Sets the tomodachi ID
     * 
     * @param tomodachiID The ID
     */
    public void setTomodachiModel(TomodachiFile tomodachiFile) {
        this.tomodachiFile.set(tomodachiFile);
    }

    /**
     * @return The current tomodachi ID or null if none was selected.
     */
    public TomodachiFile getTomodachiModel() {
        return tomodachiFile.get();
    }

    /**
     * @return The tomodachiSettings
     */
    public TomodachiSettings getTomodachiSettings() {
        return tomodachiSettings;
    }

    /**
     * Loads all possible tomodachi files and loads it in the
     * {@link #tomodachiFiles} list
     */
    public void loadPossibleTomodachiFiles() {
        throw new UnsupportedOperationException("not implemented yet");
    }
}
