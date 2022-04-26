package ch.zhaw.ikitomo.common.settings;

import java.io.IOException;

/**
 * A helper class to load and save {@link Settings} objects
 */
public class SettingsLoader {
    /**
     * The default path for the ikitomo settings
     */
    public static final String DEFAULT_SETTINGS_PATH = "./settings";

    /**
     * Private constructor
     */
    private SettingsLoader() {
    }

    /**
     * Loads the settings from the given file.
     * 
     * @param file The file to load the settings from
     * @return The loaded settings
     * @throws IOException Occurs when the file could not be read
     */
    public static Settings load(String path) throws IOException {
        throw new UnsupportedOperationException("not implemented yet");
    }

    /**
     * Saves the settings to the given file.
     * 
     * @param settings The settings to save
     * @param path     The name of the file to save the settings to
     * @throws IOException Occurs when the file could not be written
     */
    public static void save(String path, Settings settings) throws IOException {
        throw new UnsupportedOperationException("not implemented yet");
    }
}
