package ch.zhaw.ikitomo.common.settings;

import ch.zhaw.ikitomo.common.tomodachi.TomodachiFile;
import ch.zhaw.ikitomo.common.tomodachi.TomodachiSettings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Represents the settings of the application.
 */
public class Settings {
    /**
     * The id of the currently selected tomodachi
     */
    private StringProperty tomodachiID = new SimpleStringProperty(); 

    /**
     * The current settings of the tomodachi.
     */
    private TomodachiSettings tomodachiSettings;

    /**
     * Initializes a new instance of the {@link Settings} class
     */
    public Settings() {
    }

    /**
     * Constructor
     *
     * @param tomodachiSettings The tomodachi settings
     */
    public Settings(TomodachiSettings tomodachiSettings) {
        this.tomodachiSettings = tomodachiSettings;
    }

    /**
     * Gets the id of the currently selected tomodachi
     *
     * @return The id of the currently selected tomodachi
     */
    public String getTomodachiID() {
        return tomodachiID.get();
    }

    /**
     * Sets the id of the currently selected tomodachi
     *
     * @param tomodachiID The id of the currently selected tomodachi
     */
    public void setTomodachiID(String value) {
        this.tomodachiID.set(value);
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
     * Gets a property holding the id of the currently selected tomodachi
     *
     * @return A property holding the id of the currently selected tomodachi
     */
    public StringProperty tomodachiIDProperty() {
        return tomodachiID;
    }

    /**
     * Loads all available tomodachi files and stores them inside the
     * {@link #tomodachiFiles} list
     */
    public void loadPossibleTomodachiFiles() {
        throw new UnsupportedOperationException("not implemented yet");
    }
}
