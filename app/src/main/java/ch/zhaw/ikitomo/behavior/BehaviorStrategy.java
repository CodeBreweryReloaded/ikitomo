package ch.zhaw.ikitomo.behavior;

import ch.zhaw.ikitomo.common.StateType;
import ch.zhaw.ikitomo.common.tomodachi.TomodachiEnvironment;
import ch.zhaw.ikitomo.overlay.model.TomodachiModel;

public interface BehaviorStrategy {
    public void update(long now);

    public void animationFinished(StateType oldState);

    public void reset();

    public static BehaviorStrategy createInstance(TomodachiModel model, TomodachiEnvironment env) {
        return new DefaultBehaviorStrategy(model, env);
    }
}
