package ch.zhaw.ikitomo.behavior;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.random.RandomGenerator;

import com.google.common.util.concurrent.Service.State;

import ch.zhaw.ikitomo.common.Direction;
import ch.zhaw.ikitomo.common.StateType;
import ch.zhaw.ikitomo.common.Vector2;

/**
 * The default behavior strategy for tomodachis
 */
public class TomodachiBehavior {
    /**
     * Logger
     */
    private static final Logger LOGGER = Logger.getLogger(TomodachiBehavior.class.getName());

    /**
     * The distance when the tomodachi changes from the idle state to the run state
     */
    private static final float DISTANCE_BEFORE_START_MOVING = 200;
    /**
     * The distance when the tomodachi changes from the run state to the idle state
     */
    private static final float DISTANCE_TO_STOP_MOVING = 100;

    /**
     * The random instance
     */
    private RandomGenerator random = new Random();

    /**
     * The behavior model
     */
    private BehaviorModel model;

    /**
     * Constructor
     * 
     * @param tomodachiModel The tomodachi model controlled by this strategy
     * @param environment    The tomodachi environment
     */
    public TomodachiBehavior(BehaviorModel model) {
        this.model = model;
    }

    /**
     * This method is called every JavaFX pulse by an
     * {@link javafx.animation.AnimationTimer}
     * 
     * @param delta The time in milliseconds since the last pulse
     */
    public void update(double delta) {
        StateType currentState = model.getCurrentAnimationState();
        if (currentState == StateType.RUN) {
            doRunState(delta);
        }
        if (currentState == StateType.IDLE && getDistanceToNextPosition() > DISTANCE_BEFORE_START_MOVING) {
            doRunState(delta);
        }
    }

    /**
     * Calculates the new position of the tomodachi. It moves towards the position
     * returned by {@link #nextPositionStrategy}
     */
    private void doRunState(double delta) {
        // change to idle state if the tomodachi is close enough to the next position
        if (getDistanceToNextPosition() < DISTANCE_TO_STOP_MOVING) {
            model.setCurrentAnimation(StateType.IDLE);
        } else {
            Vector2 oldPosition = model.getPosition();
            Vector2 target = model.getNextPosition();
            Vector2 diff = target.subtract(oldPosition).normalize()
                    .multiply(model.getSettings().getSpeed() * delta);
            Direction direction = diff.direction();
            var nextPosition = oldPosition.add(diff);
            LOGGER.log(Level.FINE, "next position is {0}", nextPosition);
            model.setPosition(nextPosition);
            model.setCurrentAnimation(StateType.RUN, direction);
        }
    }

    /**
     * This method is called when an animation finished playing
     * 
     * @param oldState The animation state
     */
    public void animationFinished(StateType oldState) {
        StateType nextState = switch (oldState) {
            case IDLE -> isYawning() ? StateType.YAWN : StateType.IDLE;
            case RUN -> isYawning() ? StateType.YAWN : StateType.RUN;
            case SLEEP -> isWakingUp() ? StateType.WAKE : StateType.SLEEP;
            case WAKE -> StateType.IDLE;
            case YAWN -> StateType.SLEEP;
            case EAT -> StateType.IDLE;
            default -> {
                LOGGER.log(Level.WARNING, "Unknown state: {0}", oldState.name());
                yield StateType.IDLE;
            }
        };
        model.setCurrentAnimation(nextState);
        if (nextState != oldState) {
            LOGGER.log(Level.INFO, "Changed state from {0} to {1}", new Object[] { oldState.name(), nextState.name() });
        }
    }

    /**
     * This method is called when the tomodachi was clicked by the user
     */
    public void tomodachiWasClicked() {
        model.setCurrentAnimation(StateType.EAT);
    }

    /**
     * Sets the random generator
     * 
     * @param random The random
     */
    void setRandom(RandomGenerator random) {
        this.random = random;
    }

    /**
     * Calculates the distance between the tomodachi and the position returned by
     * {@link #nextPositionStrategy}
     * 
     * @return The distance to the next target position
     */
    private double getDistanceToNextPosition() {
        Vector2 nextPosition = model.getNextPosition();
        Vector2 current = model.getPosition();
        return nextPosition.subtract(current).absolute();
    }

    /**
     * If the tomodachi should wake up then the method returns true
     * 
     * @return If the tomodachi should wake up
     */
    private boolean isWakingUp() {
        return model.getSettings().getWakeChance() > random.nextFloat();
    }

    /**
     * If the tomodachi should yawn then the method returns true
     * 
     * @return If the tomodachi should yawn
     */
    private boolean isYawning() {
        return model.getSettings().getSleepChance() > random.nextFloat();
    }

}
