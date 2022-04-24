package ch.zhaw.ikitomo.common.settings;

import java.io.IOException;

/**
 * A helper class to load and save {@link Settings} objects
 */
public class SettingsLoader {
    /**
     * the default path for the ikitomo settings
     */
    public static final String DEFAULT_SETTINGS_PATH = "./settings";

    /**
     * private constructor
     */
    private SettingsLoader() {
    }

    /**
     * Loads the settings from the given file.
     * 
     * @param file the file to load from
     * @return the loaded settings
     * @throws IOException if the file could not be read
     */
    public static Settings load(String path) throws IOException {
        throw new UnsupportedOperationException("not implemented yet");
    }

    /**
     * Saves the settings to the given file.
     * 
     * @param settings the settings to save
     * @param file     the file to save to
     * @throws IOException if the file could not be written
     */
    public static void save(String path, Settings settings) throws IOException {
        throw new UnsupportedOperationException("not implemented yet");
    }
}
