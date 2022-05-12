package ch.zhaw.ikitomo.behavior;

import java.util.Objects;

import ch.zhaw.ikitomo.common.Direction;
import ch.zhaw.ikitomo.common.StateType;
import ch.zhaw.ikitomo.common.Vector2;
import ch.zhaw.ikitomo.common.tomodachi.TomodachiSettings;
import ch.zhaw.ikitomo.overlay.model.TomodachiModel;

/**
 * This is the model for {@link TomodachiBehaviour}. This class is a layer
 * between the {@link TomodachiModel} and the {@link TomodachiBehaviour}. It
 * primarily exists to reduce the api surface for mock testing
 */
public class BehaviourModel {
    /**
     * Holds the Tomodachi model instance
     */
    private TomodachiModel tomodachiModel;

    /**
     * Stores the strategy that returns next position
     */
    private NextPositionStrategy nextPositionStrategy;

    /**
     * Constructor
     * 
     * @param model The model to animate
     */
    public BehaviourModel(TomodachiModel model) {
        this.tomodachiModel = model;
        // calculate new NextPositionStrategy when the factory changes
        calculateNextPositionStrategy();
        this.tomodachiModel.getSettings().nextPositionStrategyFactoryProperty()
                .addListener((observable, oldValue, newValue) -> calculateNextPositionStrategy());
    }

    /**
     * Returns the used Tomodachi settings
     * 
     * @return The settings
     * @see TomodachiModel#getSettings()
     */
    public TomodachiSettings getSettings() {
        return tomodachiModel.getSettings();
    }

    /**
     * Sets the Tomodachi position
     * 
     * @param position
     * @see TomodachiModel#setPosition(Vector2)
     */
    public void setPosition(Vector2 position) {
        tomodachiModel.setPosition(position);
    }

    /**
     * Returns the current position of the Tomodachi
     * 
     * @return The current position vector
     */
    public Vector2 getPosition() {
        return tomodachiModel.getPosition();
    }

    /**
     * Changes the animation state of the Tomodachi
     * 
     * @param state The state to change to
     */
    public void setCurrentAnimation(StateType state) {
        tomodachiModel.setCurrentAnimation(state);
    }

    /**
     * Changes the animation state and direction of the Tomodachi
     * 
     * @param state     The state to change to
     * @param direction The direction to set
     */
    public void setCurrentAnimation(StateType state, Direction direction) {
        tomodachiModel.setCurrentAnimation(state, direction);
    }

    /**
     * Gets the current animation state
     * 
     * @return The current state
     * @see TomodachiModel#getCurrentAnimationState()
     */
    public StateType getCurrentAnimationState() {
        return tomodachiModel.getCurrentAnimationState();
    }

    /**
     * Gets the current animation direction
     * 
     * @return The current direction
     * @see TomodachiModel#getCurrentAnimationDirection()
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
