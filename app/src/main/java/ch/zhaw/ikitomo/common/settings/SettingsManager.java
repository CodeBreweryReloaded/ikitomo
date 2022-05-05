package ch.zhaw.ikitomo.common.settings;

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
}
