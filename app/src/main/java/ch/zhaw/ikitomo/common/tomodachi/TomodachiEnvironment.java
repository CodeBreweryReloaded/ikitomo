package ch.zhaw.ikitomo.common.tomodachi;

import ch.zhaw.ikitomo.common.settings.SettingsManager;

/**
 * Represents the environment of the tomodachi application
 */
public class TomodachiEnvironment {
    /**
     * A component for loading and saving settings
     */
    private SettingsManager settingsManager;

    /**
     * A component for loading and saving tomodachi definitions
     */
    private TomodachiManager tomodachiManager;

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
}
