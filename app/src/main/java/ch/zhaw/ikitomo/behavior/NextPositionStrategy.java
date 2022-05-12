package ch.zhaw.ikitomo.behavior;

import ch.zhaw.ikitomo.common.Vector2;
import ch.zhaw.ikitomo.overlay.model.TomodachiModel;

/**
 * A strategy interface which returns the next position to which the tomodachi
 * should run
 */
public interface NextPositionStrategy {
    /**
     * Returns the next position where the tomodachi should run to
     * 
     * @param t the tomodachi model which will run towards the returned position
     * @return the next position
     * @implNote The returned position is the next target and the tomodachi will
     *           move towards it. This method is called on every frame when the
     *           tomodachi is running. The position can change for every frame.
     */
    public Vector2 nextPosition(TomodachiModel t);
}
