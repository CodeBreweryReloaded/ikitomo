package ch.zhaw.ikitomo.trayicon;

import java.util.concurrent.CompletableFuture;

import ch.zhaw.ikitomo.common.Killable;

/**
 * The tray icon controller
 */
public class TrayIconController implements Killable {

    @Override
    public CompletableFuture<Void> kill() {
        return CompletableFuture.completedFuture(null);
    }

}
