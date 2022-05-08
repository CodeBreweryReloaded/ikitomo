package ch.zhaw.ikitomo.behavior;

import ch.zhaw.ikitomo.common.StateType;
import ch.zhaw.ikitomo.common.tomodachi.TomodachiEnvironment;
import ch.zhaw.ikitomo.overlay.model.TomodachiModel;

/**
 * A strategy implementing the behavior of a tomodachi
 */
public interface BehaviorStrategy {
    /**
     * Is called on every puls of the animation timer
     * 
     * @param now The time of the puls in nano seconds
     */
    public void update(long now);

    /**
     * Is called when an animation finished playing
     * 
     * @param oldState The state of the animation which ended
     */
    public void animationFinished(StateType oldState);

    /**
     * If the strategy should reset its internal state
     */
    public void reset();

    /**
     * Creates a new instance of a behavior strategy
     * 
     * @param model The tomodachi model to control
     * @param env   The environment
     * @return The created behavior strategy
     */
    public static BehaviorStrategy createInstance(TomodachiModel model, TomodachiEnvironment env) {
        return new DefaultBehaviorStrategy(model, env);
    }
}
