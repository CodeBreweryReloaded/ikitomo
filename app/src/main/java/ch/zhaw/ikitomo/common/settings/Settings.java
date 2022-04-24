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
     * the possible tomodachi files
     */
    private ObservableList<TomodachiFile> tomodachiFiles = FXCollections.observableArrayList();
    /**
     * the currently seleted tomodachi model
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
     * constructor
     * 
     * @param tomodachiID       the current id
     * @param tomodachiSettings the tomodachi settings
     */
    public Settings(TomodachiFile tomodachiFile, TomodachiSettings tomodachiSettings) {
        this.tomodachiFile.set(tomodachiFile);
        this.tomodachiSettings = tomodachiSettings;
    }

    /**
     * @return the tomodachiFiles
     */
    public ObservableList<TomodachiFile> getTomodachiFiles() {
        return tomodachiFiles;
    }

    /**
     * @return the tomodachi ID property
     */
    public ObjectProperty<TomodachiFile> tomodachiFileProperty() {
        return tomodachiFile;
    }

    /**
     * Sets the tomodachi id
     * 
     * @param tomodachiID the id
     */
    public void setTomodachiModel(TomodachiFile tomodachiFile) {
        this.tomodachiFile.set(tomodachiFile);
    }

    /**
     * @return the current tomodachi id or null if none was selected.
     */
    public TomodachiFile getTomodachiModel() {
        return tomodachiFile.get();
    }

    /**
     * @return the tomodachiSettings
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
