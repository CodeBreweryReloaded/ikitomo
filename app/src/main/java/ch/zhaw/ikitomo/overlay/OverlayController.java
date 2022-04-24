package ch.zhaw.ikitomo.overlay;

import java.util.concurrent.CompletableFuture;

import ch.zhaw.ikitomo.common.Killable;
import ch.zhaw.ikitomo.common.settings.Settings;

/**
 * The controller for the overlay which displays the tomodachi
 */
public class OverlayController implements Killable {
    /**
     * the global settings object
     */
    private Settings settings;

    /**
     * private constructor
     * 
     * @param settings the global settings object
     */
    private OverlayController(Settings settings) {
        this.settings = settings;
    }

    @Override
    public CompletableFuture<Void> kill() {
        return CompletableFuture.completedFuture(null);
    }

    /**
     * Creates a new overlay ui and returns the controller
     * 
     * @param settings the global settings object
     * @return the newly created {@link OverlayController}
     */
    public static OverlayController newOverlayUI(Settings settings) {
        throw new UnsupportedOperationException("not implemented yet");
    }
}
