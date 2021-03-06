package ch.zhaw.ikitomo.common;

import java.util.concurrent.CompletableFuture;

/**
 * A class which can be killed/closed or shutdown.
 * <p>
 * The class can do finalizations in a separate thread and complete the future when its done
 * </p>
 */
public interface Killable {
    /**
     * Kill this object.
     * <p>
     * After the finalizations are finished the future has to be completed.
     * </p>
     * 
     * @return The future which is completed after the process is finished.
     */
    public CompletableFuture<Void> kill();
}
