package ch.zhaw.ikitomo.common;

import java.util.concurrent.CompletableFuture;

/**
 * A class which can be killed/closed or shutdown. The class can do
 * finalizations in a separate thread and complete the future when its done
 */
public interface Killable {
    /**
     * Kill this object. After the finalizations are finished the future has to be
     * completed.
     * 
     * @return The future which is completed after the class is finished
     */
    public CompletableFuture<Void> kill();
}
