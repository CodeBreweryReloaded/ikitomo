package ch.zhaw.ikitomo.overlay.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ch.zhaw.ikitomo.behavior.NextPositionStrategy;
import ch.zhaw.ikitomo.behavior.NextPositionStrategyFactory;
import ch.zhaw.ikitomo.common.Direction;
import ch.zhaw.ikitomo.common.StateType;
import ch.zhaw.ikitomo.common.Vector2;
import ch.zhaw.ikitomo.common.tomodachi.TomodachiDefinition;
import ch.zhaw.ikitomo.common.tomodachi.TomodachiEnvironment;
import ch.zhaw.ikitomo.common.tomodachi.TomodachiSettings;
import ch.zhaw.ikitomo.overlay.model.animation.AnimationData;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

/**
 * Represents a the model for the
 * {@link ch.zhaw.ikitomo.overlay.OverlayController}.
 * <p>
 * This model also includes loaded resources like animations and fields needed
 * to display a Tomodachi, such as position and velocity.
 * </p>
 */
public class TomodachiModel {

    /**
     * The id of the tomodachi
     */
    private String id;
    /**
     * The name of the tomodachi
     */
    private String name;

    /**
     * The tomodachi settings
     */
    private TomodachiSettings settings;

    /**
     * The position property
     */
    private ObjectProperty<Vector2> position = new SimpleObjectProperty<>(Vector2.ZERO);

    /**
     * The current state of the animation
     */
    private ObjectProperty<StateType> currentAnimationState = new SimpleObjectProperty<>(StateType.IDLE);

    /**
     * The current direction of the animation
     */
    private ObjectProperty<Direction> currentAnimationDirection = new SimpleObjectProperty<>(Direction.NONE);

    /**
     * The available states of the tomodachi
     */
    private List<TomodachiModelState> states = new ArrayList<>();

    /**
     * A hash map containing all available animations for each {@link StateType}
     */
    private ObservableMap<StateType, List<AnimationData>> animations = FXCollections.observableHashMap();

    /**
     * Constructor
     * 
     * @param id       The id
     * @param name     The name
     * @param position The position
     * @param velocity The velocity
     * @param states   The states
     */
    public TomodachiModel(TomodachiDefinition definition, Map<StateType, List<AnimationData>> animations) {
        this.id = definition.getID();
        this.name = definition.getName();
        this.settings = definition.getSettings();
        this.animations.putAll(animations);
    }

    /**
     * Gets the id of the tomodachi
     *
     * @return The id of the tomodachi
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the name of the tomodachi
     *
     * @return The name of the tomodachi
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the tomodachi settings
     * 
     * @return the settings
     */
    public TomodachiSettings getSettings() {
        return settings;
    }

    /**
     * Gets a copy of the list of available states
     *
     * @return A copy of the list of available states
     */
    public List<TomodachiModelState> getStates() {
        return new ArrayList<>(states);
    }

    /**
     * Provides an observable map containing all currently loaded animations
     * 
     * @return An observable map
     */
    public ObservableMap<StateType, List<AnimationData>> getObservableAnimations() {
        return animations;
    }

    /**
     * Gets the position property
     * 
     * @return The property
     */
    public ObjectProperty<Vector2> positionProperty() {
        return position;
    }

    /**
     * Sets the position of this tomodachi
     * 
     * @param position The position
     */
    public void setPosition(Vector2 position) {
        this.position.set(position);
    }

    /**
     * Gets the current position of this tomodachi
     * 
     * @return The position
     */
    public Vector2 getPosition() {
        return position.get();
    }

    /**
     * Gets the object property for the current animation state
     * 
     * @return The property
     */
    public ObjectProperty<StateType> currentAnimationStateProperty() {
        return currentAnimationState;
    }

    /**
     * Sets the current animation state
     * 
     * @param currentAnimationState The new animation state
     */
    public void setCurrentAnimationState(StateType currentAnimationState) {
        this.currentAnimationState.set(currentAnimationState);
    }

    /**
     * Gets the current animation state
     * 
     * @return The current state
     */
    public StateType getCurrentAnimationState() {
        return currentAnimationState.get();
    }

    /**
     * Gets the current direction of the animation
     * 
     * @return The direction
     */
    public ObjectProperty<Direction> currentAnimationDirectionProperty() {
        return currentAnimationDirection;
    }

    /**
     * Sets the current animation direction
     * 
     * @param currentAnimationDirection The direction
     */
    public void setCurrentAnimationDirection(Direction currentAnimationDirection) {
        this.currentAnimationDirection.set(currentAnimationDirection);
    }

    /**
     * Gets the current animation direction
     * 
     * @return The direction
     */
    public Direction getCurrentAnimationDirection() {
        return currentAnimationDirection.get();
    }

    /**
     * Sets the current animation by setting the state and direction of the
     * tomodachi
     * 
     * @param state     the new state
     * @param direction the direction
     */
    public void setCurrentAnimation(StateType state, Direction direction) {
        currentAnimationState.set(state);
        currentAnimationDirection.set(direction);
    }

    /**
     * Sets the current animation by setting the state. The direction is set to
     * {@link Direction#NONE}
     * 
     * @param state the new state
     */
    public void setCurrentAnimation(StateType state) {
        setCurrentAnimation(state, Direction.NONE);
    }

    /**
     * Creates a new {@link NextPositionStrategy} from the
     * {@link NextPositionStrategyFactory} set in the {@link TomodachiSettings}
     * 
     * @param env The tomodachi environment
     * @return The created strategy
     * @see NextPositionStrategyFactory#createNextPosition(TomodachiEnvironment)
     */
    public NextPositionStrategy createNextPointStrategy() {
        return getSettings().getNextPositionStrategyFactory().createNextPosition();
    }
}
