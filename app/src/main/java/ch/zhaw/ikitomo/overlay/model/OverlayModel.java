package ch.zhaw.ikitomo.overlay.model;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ch.zhaw.ikitomo.behavior.BehaviorModel;
import ch.zhaw.ikitomo.behavior.TomodachiBehavior;
import ch.zhaw.ikitomo.common.Direction;
import ch.zhaw.ikitomo.common.JFXUtils;
import ch.zhaw.ikitomo.common.StateType;
import ch.zhaw.ikitomo.common.Vector2;
import ch.zhaw.ikitomo.common.tomodachi.TomodachiEnvironment;
import ch.zhaw.ikitomo.exception.MissingAnimationException;
import ch.zhaw.ikitomo.overlay.model.TomodachiModel.TomodachiAnimationState;
import ch.zhaw.ikitomo.overlay.model.animation.AnimationData;
import javafx.animation.AnimationTimer;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

/**
 * The model for the {@link ch.zhaw.ikitomo.overlay.OverlayController}
 */
public class OverlayModel {
    private static final Logger LOGGER = Logger.getLogger(OverlayModel.class.getName());

    /**
     * The current tomodachi environment object
     */
    private TomodachiEnvironment environment;

    /**
     * The currently loaded tomodachi
     */
    private ObjectBinding<TomodachiModel> tomodachi;

    /**
     * A binding to the current behavior strategy. It is dependent on
     * {@link #tomodachi}
     */
    private ObjectBinding<TomodachiBehavior> behaviorStrategy;

    /**
     * A binding to the current position of the tomodachi
     */
    private ObjectBinding<Vector2> tomodachiPosition;

    /**
     * A binding to the state of the current tomodachi
     */
    private ObjectBinding<TomodachiAnimationState> tomodachiState;

    /**
     * The animation timer for the behavior
     */
    private AnimationTimer behaviorTimer = new AnimationTimer() {
        private long lastUpdate = 0;

        @Override
        public void handle(long now) {
            // if this is the first call, skip the call to BehaviorStrategy#update(delta) to
            // initialize lastUpdate
            if (lastUpdate != 0) {
                double delta = (now - lastUpdate) / 1_000_000.0;
                getBehavior().update(delta);
            }
            lastUpdate = now;
        }
    };

    /**
     * The list of animations of the current tomodachi. This list is updated in
     * {@link #updateTomodachiAnimations(TomodachiModel)}
     */
    private ObservableMap<StateType, List<AnimationData>> tomodachiAnimations = FXCollections.observableHashMap();

    /**
     * Constructor
     *
     * @param environment The global settings object
     */
    public OverlayModel(TomodachiEnvironment environment) {
        this.environment = environment;
        tomodachi = Bindings.createObjectBinding(this::loadTomodachiModel,
                environment.getSettings().tomodachiIDProperty());

        tomodachiPosition = JFXUtils.nestedBinding(tomodachi, TomodachiModel::positionProperty);
        tomodachiState = JFXUtils.nestedBinding(tomodachi, TomodachiModel::currentAnimationStateProperty);

        behaviorStrategy = Bindings
                .createObjectBinding(() -> new TomodachiBehavior(new BehaviorModel(getTomodachi())), tomodachi);
        updateTomodachiAnimations(getTomodachi());
        tomodachi.addListener((observable, oldValue, newValue) -> updateTomodachiAnimations(newValue));

        tomodachi.addListener((observable, oldValue, newValue) -> newValue.setPosition(oldValue.getPosition()));
    }

    /**
     * Updates the {@link #tomodachiAnimations} map
     * 
     * @param model the new tomodachi model
     */
    private void updateTomodachiAnimations(TomodachiModel model) {
        tomodachiAnimations.clear();
        tomodachiAnimations.putAll(model.getObservableAnimations());
    }

    /**
     * Starts the behavior animation timer
     */
    public void start() {
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
    public ObjectBinding<TomodachiBehavior> behaviorStrategyBinding() {
        return behaviorStrategy;
    }

    /**
     * Gets the current behavior strategy
     * 
     * @return The strategy
     */
    public TomodachiBehavior getBehavior() {
        return behaviorStrategy.get();
    }

    /**
     * Gets an observable map of {@link StateType}s to {@link AnimationData} lists
     * 
     * @return The animation map of the currently selected tomodachi
     */
    public ObservableMap<StateType, List<AnimationData>> getObservableAnimations() {
        return tomodachiAnimations;
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
    public ObjectBinding<TomodachiAnimationState> tomodachiStateBinding() {
        return tomodachiState;
    }

    /**
     * Gets the state of the current tomodachi
     * 
     * @return The state
     */
    public StateType getTomodachiState() {
        return tomodachiState.get().currentAnimationState();
    }

    /**
     * Gets the direction of the current tomodachi
     * 
     * @return The direction
     */
    public Direction getTomodachiDirection() {
        return tomodachiState.get().currentAnimationDirection();
    }

    /**
     * Gets the center of the primary monitor
     * 
     * @return A vector representing the center point
     */
    public Vector2 getScreenCenter() {
        Rectangle2D screen = Screen.getPrimary().getBounds();
        return new Vector2(screen.getMaxX() / 2, screen.getMaxY() / 2);
    }

    /**
     * Loads a Tomodachi model from the currently loaded Tomodachi. The default
     * Tomodachi is loaded upon failure
     * 
     * @return The loaded Tomodachi model
     */
    protected TomodachiModel loadTomodachiModel() {
        try {
            return new TomodachiModelLoader(environment.getCurrentTomodachiDefinition(), environment.getSettings())
                    .loadFromTomodachiFile();
        } catch (MissingAnimationException e) {
            LOGGER.log(Level.WARNING, "Unable to load model. Loading default fallback model", e);
            return loadDefaultTomodachiModel();
        }
    }

    /**
     * Helper method to load the default Tomodachi in case of an error. Throws a
     * runtime error when even this fails. Should not happen in any case.
     * 
     * @return The default tomodachi model
     */
    protected TomodachiModel loadDefaultTomodachiModel() {
        try {
            return new TomodachiModelLoader(environment.getDefaultTomodachiDefinition(), environment.getSettings())
                    .loadFromTomodachiFile();
        } catch (MissingAnimationException e) {
            throw new IllegalStateException("Unable to load default Tomodachi model", e);
        }
    }

}
