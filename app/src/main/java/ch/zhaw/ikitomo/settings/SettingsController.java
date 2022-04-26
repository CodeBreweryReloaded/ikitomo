package ch.zhaw.ikitomo.settings;

import java.util.concurrent.CompletableFuture;

import ch.zhaw.ikitomo.common.Killable;
import ch.zhaw.ikitomo.common.settings.Settings;

/**
 * The settings controller
 */
public class SettingsController implements Killable {
    /**
     * The global settings object
     */
    private Settings settings;

    /**
     * Private constructor
     * 
     * @param settings The global settings object
     */
    private SettingsController(Settings settings) {
        this.settings = settings;
    }

    @Override
    public CompletableFuture<Void> kill() {
        return CompletableFuture.completedFuture(null);
    }

    /**
     * Creates a new settings window based on the settings FXML and returns the
     * Settings controller
     * 
     * @param settings The global settings object
     * @return The new {@link SettingsController}
     */
    public static SettingsController newSettingsUI(Settings settings) {
        throw new UnsupportedOperationException("not implemented yet");
    }

}
