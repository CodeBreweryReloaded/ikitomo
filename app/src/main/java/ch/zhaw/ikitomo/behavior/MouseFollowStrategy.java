package ch.zhaw.ikitomo.behavior;

import java.awt.MouseInfo;
import java.awt.Point;

import ch.zhaw.ikitomo.common.Vector2;
import ch.zhaw.ikitomo.overlay.model.TomodachiModel;

/**
 * A strategy for the {@link TomodachiBehaviour} which returns for the next
 * position the mouse position
 */
public class MouseFollowStrategy implements NextPositionStrategy {

    @Override
    public Vector2 nextPosition(TomodachiModel t) {
        Point p = MouseInfo.getPointerInfo().getLocation();
        return new Vector2((int) p.getX(), (int) p.getY());
    }

}
