package ch.zhaw.ikitomo.common.settings;

import java.io.IOException;
import java.util.HashMap;

import ch.zhaw.ikitomo.common.tomodachi.TomodachiDefinition;

/**
 * A helper class to load and save {@link Settings} objects
 */
public class SettingsManager {
    /**
     * The default path for the ikitomo settings
     */
    public static final String DEFAULT_SETTINGS_PATH = "./settings";

    /**
     * Private constructor
     */
    private SettingsManager() {
    }

    /**
     * Loads the settings from the given file.
     * 
     * @param file The file to load the settings from
     * @return The loaded settings
     * @throws IOException Occurs when the file could not be read
     */
    public static Settings load(String path) throws IOException {
        // TODO: throw new UnsupportedOperationException("not implemented yet");
        return new Settings(TomodachiDefinition.createMockObject(), new HashMap<>());
    }

    /**
     * Loads the settings from the default file at {@value #DEFAULT_SETTINGS_PATH}
     * which is stored in {@link #DEFAULT_SETTINGS_PATH}
     * 
     * @return The loaded settings
     * @throws IOException Occurs when the file could not be read
     */
    public static Settings loadFromDefault() throws IOException {
        return load(DEFAULT_SETTINGS_PATH);
    }

    /**
     * Saves the settings to the given file.
     * 
     * @param path     The name of the file to save the settings to
     * @param settings The settings to save
     * @throws IOException Occurs when the file could not be written
     */
    public static void save(String path, Settings settings) throws IOException {
        throw new UnsupportedOperationException("not implemented yet");
    }

    /**
     * Saves the settings to the default path {@link #DEFAULT_SETTINGS_PATH} at
     * {@value #DEFAULT_SETTINGS_PATH}
     * 
     * @param settings The settings to save
     * @throws IOException Occurs when the file could not be written
     */
    public static void saveToDefault(Settings settings) throws IOException {
        throw new UnsupportedOperationException("not implemented yet");
    }
}
