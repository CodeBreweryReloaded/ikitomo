package ch.zhaw.ikitomo.common.settings;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import ch.zhaw.ikitomo.common.tomodachi.TomodachiDefinition;
import ch.zhaw.ikitomo.common.tomodachi.TomodachiSettings;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Represents the settings of the application.
 */
public class Settings {
    /**
     * The id of the currently selected tomodachi
     */
    @JsonIgnore
    private StringProperty tomodachiID = new SimpleStringProperty();

    /**
     * A set of tomodachi ids and their corresponding settings
     */
    @JsonProperty(SettingKey.TOMODACHI_SETTINGS)
    private Map<String, TomodachiSettings> tomodachiSettings = new HashMap<>();

    /**
     * A binding to the current selected tomodachi settings. If
     * {@link #tomodachiFileProperty()} points to a {@link TomodachiDefinition} for
     * which
     * no Settings currently exists than a new {@link TomodachiSettings} object is
     * created and added to the {@link #tomodachiSettings} map
     */
    private ObjectBinding<TomodachiSettings> currentTomodachiSettings = Bindings
            .createObjectBinding(this::getTomodachiSettings, tomodachiID);

    /**
     * Initializes a new instance of the {@link Settings} class
     */
    private Settings() {
    }

    /**
     * Initializes a new instance of the {@link Settings} class
     *
     * @param tomodachiSettings A set of tomodachi ids and their corresponding
     *                          settings
     */
    public Settings(String tomodachiID, Map<String, TomodachiSettings> tomodachiSettings) {
        this.setTomodachiID(tomodachiID);
        this.tomodachiSettings = tomodachiSettings;
    }

    /**
     * Gets the id of the currently selected tomodachi
     *
     * @return The id of the currently selected tomodachi
     */
    @JsonProperty(SettingKey.TOMODACHI_ID)
    public String getTomodachiID() {
        return tomodachiID.get();
    }

    /**
     * Sets the id of the currently selected tomodachi
     *
     * @param tomodachiID The id of the currently selected tomodachi
     */
    @JsonProperty(SettingKey.TOMODACHI_ID)
    public void setTomodachiID(String value) {
        this.tomodachiID.set(value);
    }

    /**
     * Gets the currently selected tomodachi settings
     *
     * @return The tomodachiSettings
     */
    @JsonIgnore
    public TomodachiSettings getTomodachiSettings() {
        tomodachiSettings.computeIfAbsent(getTomodachiID(), key -> new TomodachiSettings());
        return tomodachiSettings.get(getTomodachiID());
    }

    /**
     * Gets a property holding the id of the currently selected tomodachi
     *
     * @return A property holding the id of the currently selected tomodachi
     */
    @JsonIgnore
    public StringProperty tomodachiIDProperty() {
        return tomodachiID;
    }

    /**
     * Gets a binding to the current selected tomodachi settings
     * 
     * @return The binding
     */
    @JsonIgnore
    public ObjectBinding<TomodachiSettings> currentTomodachiSettingsBinding() {
        return currentTomodachiSettings;
    }

    /**
     * Creates a new default settings object
     * 
     * @return The default settings object
     */
    public static Settings createDefaultSettings() {
        return new Settings("ch.zhaw.ikitomo.defaultNeko", new HashMap<>());
    }
}
