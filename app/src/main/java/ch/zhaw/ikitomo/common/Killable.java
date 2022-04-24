package ch.zhaw.ikitomo.common;

import java.util.concurrent.CompletableFuture;

public interface Killable {
    public CompletableFuture<Void> kill();
}
