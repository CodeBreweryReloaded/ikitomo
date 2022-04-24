package ch.zhaw.ikitomo.overlay.model;

import ch.zhaw.ikitomo.common.Animation;
import ch.zhaw.ikitomo.common.StateType;

/**
 * Represents a state of the tomodachi model for the
 * {@link ch.zhaw.ikitomo.overlay.OverlayController}
 * 
 * @param type      the type of the state
 * @param animation the animation
 */
public record TomodachiModelState(StateType type, Animation animation) {
}
