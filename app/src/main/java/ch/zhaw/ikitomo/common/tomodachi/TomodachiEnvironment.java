package ch.zhaw.ikitomo.common.tomodachi;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import ch.zhaw.ikitomo.common.settings.Settings;
import ch.zhaw.ikitomo.common.settings.SettingsManager;

/**
 * Represents the environment of the tomodachi application
 */
public class TomodachiEnvironment {
    /**
     * A component for logging messages.
     */
    private static final Logger LOGGER = Logger.getLogger(TomodachiEnvironment.class.getName());

    /**
     * A component for loading and saving settings
     */
    private SettingsManager settingsManager;

    /**
     * A component for loading and saving tomodachi definitions
     */
    private TomodachiManager tomodachiManager;

    /**
     * The settings of the application
     */
    private Settings settings = null;

    /**
     * Initializes a new instance of the {@link TomodachiEnvironment} class
     *
     * @param settingsManager
     */
    public TomodachiEnvironment(SettingsManager settingsManager, TomodachiManager tomodachiManager) {
        this.settingsManager = settingsManager;
        this.tomodachiManager = tomodachiManager;
    }

    /**
     * Gets a component for loading and saving settings
     *
     * @return A component for loading and saving settings
     */
    public SettingsManager getSettingsManager() {
        return settingsManager;
    }

    /**
     * Gets a component for loading and saving tomodachi definitions
     *
     * @return A component for loading and saving tomodachi definitions
     */
    public TomodachiManager getTomodachiManager() {
        return tomodachiManager;
    }

    /**
     * Gets the settings of the application
     *
     * @return The settings of the application
     */
    public Settings getSettings() {
        return settings;
    }

    /**
     * Loads the settings from the default location
     *
     * @return The settings of the application
     * @throws IOException Occurs when the settings could not be loaded
     */
    public Settings load() {
        try {
            settings = settingsManager.load(SettingsManager.DEFAULT_SETTINGS_PATH);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "The settings could not be loaded.", e);
            settings = new Settings();
        }

        return settings;
    }

    /**
     * Saves the settings to the default location
     *
     * @throws IOException Occurs when the settings could not be saved
     */
    public void save() throws IOException {
        settingsManager.save(SettingsManager.DEFAULT_SETTINGS_PATH, settings);
    }
}
