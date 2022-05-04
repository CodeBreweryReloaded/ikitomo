package ch.zhaw.ikitomo.common.settings;

import java.io.IOException;

import ch.zhaw.ikitomo.common.JSONManager;

/**
 * A helper class to load and save {@link Settings} objects
 */
public class SettingsManager extends JSONManager<Settings> {
    /**
     * The default path for the ikitomo settings
     */
    public static final String DEFAULT_SETTINGS_PATH = "./settings";

    /**
     * Initializes a new instance of the {@link SettingsManager} class
     */
    public SettingsManager() {
        super(Settings.class);
    }

    /**
     * Loads the settings from the given file.
     * 
     * @param file The file to load the settings from
     * @return The loaded settings
     * @throws IOException Occurs when the file could not be read
     */
    public Settings load(String path) throws IOException {
        return super.load(path);
    }

    /**
     * Saves the settings to the given file.
     * 
     * @param settings The settings to save
     * @param path     The name of the file to save the settings to
     * @throws IOException Occurs when the file could not be written
     */
    public void save(String path, Settings settings) throws IOException {
        super.save(path, settings);
    }
}
