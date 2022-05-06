package ch.zhaw.ikitomo.overlay.model;

import ch.zhaw.ikitomo.common.Direction;
import ch.zhaw.ikitomo.common.StateType;

/**
 * Represents a state of the tomodachi model for the
 * {@link ch.zhaw.ikitomo.overlay.OverlayController}
 * 
 * @param type      The type of the state
 * @param animation The animation
 */
public record TomodachiModelState(StateType type, Direction direction, String animationName) {
}
