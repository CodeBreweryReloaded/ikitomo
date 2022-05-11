package ch.zhaw.ikitomo.behavior;

import java.util.Objects;

import ch.zhaw.ikitomo.common.Direction;
import ch.zhaw.ikitomo.common.StateType;
import ch.zhaw.ikitomo.common.Vector2;
import ch.zhaw.ikitomo.common.tomodachi.TomodachiSettings;
import ch.zhaw.ikitomo.overlay.model.TomodachiModel;

/**
 * This is the model for {@link TomodachiBehavior}. This class is a layer
 * between the {@link TomodachiModel} and the {@link TomodachiBehavior}. It
 * primarily exists to reduce the api surface for mock testing
 */
public class BehaviorModel {
    private TomodachiModel tomodachiModel;
    private NextPositionStrategy nextPositionStrategy;

    public BehaviorModel(TomodachiModel model) {
        this.tomodachiModel = model;
        // calculate new NextPositionStrategy when the factory changes
        calculateNextPositionStrategy();
        this.tomodachiModel.getSettings().nextPositionStrategyFactoryProperty()
                .addListener((observable, oldValue, newValue) -> calculateNextPositionStrategy());
    }

    /**
     * @return
     * @see ch.zhaw.ikitomo.overlay.model.TomodachiModel#getSettings()
     */

    public TomodachiSettings getSettings() {
        return tomodachiModel.getSettings();
    }

    /**
     * @param position
     * @see ch.zhaw.ikitomo.overlay.model.TomodachiModel#setPosition(ch.zhaw.ikitomo.common.Vector2)
     */

    public void setPosition(Vector2 position) {
        tomodachiModel.setPosition(position);
    }

    public Vector2 getPosition() {
        return tomodachiModel.getPosition();
    }

    public void setCurrentAnimation(StateType state) {
        tomodachiModel.setCurrentAnimation(state);
    }

    public void setCurrentAnimation(StateType state, Direction direction) {
        tomodachiModel.setCurrentAnimation(state, direction);
    }

    /**
     * @return
     * @see ch.zhaw.ikitomo.overlay.model.TomodachiModel#getCurrentAnimationState()
     */

    public StateType getCurrentAnimationState() {
        return tomodachiModel.getCurrentAnimationState();
    }

    /**
     * @return
     * @see ch.zhaw.ikitomo.overlay.model.TomodachiModel#getCurrentAnimationDirection()
     */

    public Direction getCurrentAnimationDirection() {
        return tomodachiModel.getCurrentAnimationDirection();
    }

    /**
     * Creates a new {@link NextPositionStrategy} from the factory sets it to
     * {@link #nextPositionStrategy}
     */
    private void calculateNextPositionStrategy() {
        nextPositionStrategy = tomodachiModel.createNextPointStrategy();
        Objects.requireNonNull(nextPositionStrategy, "NextPositionStrategy must not be null");
    }

    /**
     * Gets the next position from the {@link #nextPositionStrategy}
     * 
     * @return the next target position
     */
    public Vector2 getNextPosition() {
        return nextPositionStrategy.nextPosition(tomodachiModel);
    }

}
