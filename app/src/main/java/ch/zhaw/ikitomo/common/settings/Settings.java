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
     * The available tomodachi files
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
     * Initializes a new instance of the {@link Settings} class
     */
    public Settings() {
        this.tomodachiSettings = new TomodachiSettings();
    }

    /**
     * Constructor
     * 
     * @param tomodachiID       The id of the tomodachi to display
     * @param tomodachiSettings The tomodachi settings
     */
    public Settings(TomodachiFile tomodachiFile, TomodachiSettings tomodachiSettings) {
        this.tomodachiFile.set(tomodachiFile);
        this.tomodachiSettings = tomodachiSettings;
    }

    /**
     * Gets the available tomodachi files
     *
     * @return The available tomodachi files
     */
    public ObservableList<TomodachiFile> getTomodachiFiles() {
        return tomodachiFiles;
    }

    /**
     * Gets a property holding the tomodachi id
     *
     * @return A property holding the tomodachi id
     */
    public ObjectProperty<TomodachiFile> tomodachiFileProperty() {
        return tomodachiFile;
    }

    /**
     * Sets the id of the tomodachi to display
     * 
     * @param tomodachiID The id of the tomodachi to display
     */
    public void setTomodachiModel(TomodachiFile tomodachiFile) {
        this.tomodachiFile.set(tomodachiFile);
    }

    /**
     * Gets the id of the tomodachi to display or <code>null</code> if none was selected
     *
     * @return The id of the tomodachi to display or <code>null</code> if none was selected
     */
    public TomodachiFile getTomodachiModel() {
        return tomodachiFile.get();
    }

    /**
     * Gets the tomodachi settings
     *
     * @return The tomodachiSettings
     */
    public TomodachiSettings getTomodachiSettings() {
        return tomodachiSettings;
    }

    /**
     * Loads all available tomodachi files and stores them inside the
     * {@link #tomodachiFiles} list
     */
    public void loadPossibleTomodachiFiles() {
        throw new UnsupportedOperationException("not implemented yet");
    }
}
