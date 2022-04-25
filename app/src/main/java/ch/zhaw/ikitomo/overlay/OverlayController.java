package ch.zhaw.ikitomo.overlay;

import java.util.concurrent.CompletableFuture;

import ch.zhaw.ikitomo.common.Killable;
import ch.zhaw.ikitomo.common.settings.Settings;

/**
 * The controller for the overlay that displays the Tomodachi.
 */
public class OverlayController implements Killable {
    /**
     * The global settings object
     */
    private Settings settings;

    /**
     * Private constructor
     * 
     * @param settings The global settings object
     */
    private OverlayController(Settings settings) {
        this.settings = settings;
    }

    @Override
    public CompletableFuture<Void> kill() {
        return CompletableFuture.completedFuture(null);
    }

    /**
     * Creates a new overlay UI and returns the controller
     * 
     * @param settings The global settings object
     * @return The newly created {@link OverlayController}
     */
    public static OverlayController newOverlayUI(Settings settings) {
        throw new UnsupportedOperationException("not implemented yet");
    }
}
