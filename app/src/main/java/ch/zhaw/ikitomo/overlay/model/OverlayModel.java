package ch.zhaw.ikitomo.overlay.model;

import java.util.List;

import ch.zhaw.ikitomo.behavior.BehaviorStrategy;
import ch.zhaw.ikitomo.common.Direction;
import ch.zhaw.ikitomo.common.JavaFXUtils;
import ch.zhaw.ikitomo.common.StateType;
import ch.zhaw.ikitomo.common.Vector2;
import ch.zhaw.ikitomo.common.tomodachi.TomodachiEnvironment;
import ch.zhaw.ikitomo.overlay.OverlayController;
import ch.zhaw.ikitomo.overlay.model.animation.AnimationData;
import javafx.animation.AnimationTimer;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.collections.ObservableMap;

/**
 * The model for the {@link ch.zhaw.ikitomo.overlay.OverlayController}
 */
public class OverlayModel {

    /**
     * The current tomodachi environment object
     */
    private TomodachiEnvironment environment;
    /**
     * A reference to the controller that created this model
     */
    private OverlayController controller;
    /**
     * The currently loaded tomodachi
     */
    private ObjectBinding<TomodachiModel> tomodachi;

    /**
     * A binding to the current behavior strategy. It is dependent on
     * {@link #tomodachi}
     */
    private ObjectBinding<BehaviorStrategy> behaviorStrategy;

    /**
     * A binding to the current position of the tomodachi
     */
    private ObjectBinding<Vector2> tomodachiPosition;

    /**
     * A binding to the state of the current tomodachi
     */
    private ObjectBinding<StateType> tomodachiState;

    /**
     * A binding to the direction of the current tomodachi
     */
    private ObjectBinding<Direction> tomodachiDirection;

    /**
     * The animation timer for the behavior
     */
    private AnimationTimer behaviorTimer = new AnimationTimer() {

        @Override
        public void handle(long now) {
            getBehaviorStrategy().update(now);
        }
    };

    /**
     * Constructor
     *
     * @param environment The global settings object
     */
    public OverlayModel(TomodachiEnvironment environment, OverlayController controller) {
        this.environment = environment;
        this.controller = controller;
        tomodachi = Bindings.createObjectBinding(this::loadTomodachiModel,
                environment.getSettings().tomodachiIDProperty());

        tomodachiPosition = JavaFXUtils.nestedBinding(tomodachi, TomodachiModel::positionProperty);
        tomodachiState = JavaFXUtils.nestedBinding(tomodachi, TomodachiModel::currentAnimationStateProperty);
        tomodachiDirection = JavaFXUtils.nestedBinding(tomodachi, TomodachiModel::currentAnimationDirectionProperty);

        behaviorStrategy = Bindings
                .createObjectBinding(() -> BehaviorStrategy.createInstance(getTomodachi(), environment), tomodachi);

        behaviorTimer.start();
    }

    /**
     * Gets the binding of the currently selected tomodachi
     *
     * @return The binding of the currently selected tomodachi
     */
    public ObjectBinding<TomodachiModel> tomodachiBinding() {
        return tomodachi;
    }

    /**
     * Gets the currently selected tomodachi
     *
     * @return The currently selected tomodachi
     */
    public TomodachiModel getTomodachi() {
        return tomodachi.get();
    }

    /**
     * Gets the binging of the current behavior strategy
     * 
     * @return The binging to the strategy
     */
    public ObjectBinding<BehaviorStrategy> behaviorStrategyBinding() {
        return behaviorStrategy;
    }

    /**
     * Gets the current behavior strategy
     * 
     * @return The strategy
     */
    public BehaviorStrategy getBehaviorStrategy() {
        return behaviorStrategy.get();
    }

    /**
     * Gets an observable map of {@link StateType}s to {@link AnimationData} lists
     * 
     * @return
     */
    public ObservableMap<StateType, List<AnimationData>> getObservableAnimations() {
        return tomodachi.get().getObservableAnimations();
    }

    /**
     * Gets the binding to the current tomodachi position
     * 
     * @return The binding binding
     */
    public ObjectBinding<Vector2> tomodachiPositionBinding() {
        return tomodachiPosition;
    }

    /**
     * Gets the current position of the current tomodachi
     * 
     * @return The position
     */
    public Vector2 getTomodachiPosition() {
        return tomodachiPosition.get();
    }

    /**
     * Gets a binding to the current tomodachi state
     * 
     * @return The binding
     */
    public ObjectBinding<StateType> tomodachiStateBinding() {
        return tomodachiState;
    }

    /**
     * Gets the state of the current tomodachi
     * 
     * @return The state
     */
    public StateType getTomodachiState() {
        return tomodachiState.get();
    }

    /**
     * Gets the binding to the direction of the current tomodachi
     * 
     * @return The binding
     */
    public ObjectBinding<Direction> tomodachiDirectionBinding() {
        return tomodachiDirection;
    }

    /**
     * Gets the direction of the current tomodachi
     * 
     * @return The direction
     */
    public Direction getTomodachiDirection() {
        return tomodachiDirection.get();
    }

    /**
     * Loads a Tomodachi model from the currently loaded Tomodachi
     * 
     * @return The loaded Tomodachi model
     */
    private TomodachiModel loadTomodachiModel() {
        return new TomodachiModelLoader(environment.getCurrentTomodachiDefinition(), controller.getScreenCenter())
                .loadFromTomodachiFile();
    }

}
