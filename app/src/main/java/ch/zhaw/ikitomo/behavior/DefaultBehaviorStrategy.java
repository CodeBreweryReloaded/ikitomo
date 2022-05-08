package ch.zhaw.ikitomo.behavior;

import java.util.Objects;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import ch.zhaw.ikitomo.common.Direction;
import ch.zhaw.ikitomo.common.StateType;
import ch.zhaw.ikitomo.common.Vector2;
import ch.zhaw.ikitomo.common.tomodachi.TomodachiEnvironment;
import ch.zhaw.ikitomo.overlay.model.TomodachiModel;

/**
 * The default behavior strategy for tomodachis
 */
public class DefaultBehaviorStrategy implements BehaviorStrategy {
    /**
     * Logger
     */
    private static final Logger LOGGER = Logger.getLogger(DefaultBehaviorStrategy.class.getName());

    /**
     * The distance when the tomodachi changes from the idle state to the run state
     */
    private static final float DISTANCE_BEFORE_START_MOVING = 200;

    /**
     * The random instance
     */
    private Random random = new Random();

    /**
     * The tomodachi environment
     */
    private TomodachiEnvironment environment;

    /**
     * The tomodachi model which is controlled by this strategy
     */
    private TomodachiModel tomodachiModel;

    /**
     * The strategy to get the next target in the running state
     */
    private NextPositionStrategy nextPositionStrategy;

    /**
     * Constructor
     * 
     * @param tomodachiModel The tomodachi model controlled by this strategy
     * @param environment    The tomodachi environment
     */
    public DefaultBehaviorStrategy(TomodachiModel tomodachiModel, TomodachiEnvironment environment) {
        this.tomodachiModel = tomodachiModel;
        this.environment = environment;

        calculateNextPositionStrategy();

        // calculate new NextPositionStrategy when the factory changes
        this.tomodachiModel.getSettings().nextPositionStrategyFactoryProperty()
                .addListener((observable, oldValue, newValue) -> calculateNextPositionStrategy());
    }

    @Override
    public void update(long now) {
        StateType currentState = tomodachiModel.getCurrentAnimationState();
        switch (currentState) {
            case RUN:
                doRunState();
                break;
            case IDLE:
            case SLEEP:
            case YAWN:
            case WAKE:
                tomodachiModel.setCurrentAnimationDirection(Direction.NONE);
                break;
            default:
                LOGGER.log(Level.WARNING, "Unknown state: {0}", currentState.name());
                break;
        }
    }

    /**
     * Calculates the new position of the tomodachi. It moves towards the position
     * returned by {@link #nextPositionStrategy}
     */
    private void doRunState() {
        Vector2 oldPosition = tomodachiModel.getPosition();
        Vector2 target = getNextPosition();
        Vector2 diff = target.subtract(oldPosition).normalize().multiply(tomodachiModel.getSettings().getSpeed());
        Direction direction = diff.direction();
        tomodachiModel.setPosition(oldPosition.add(diff));
        tomodachiModel.setCurrentAnimationDirection(direction);
    }

    @Override
    public void animationFinished(StateType oldState) {
        StateType nextState = switch (oldState) {
            case IDLE -> getNextStateAfterIdle();
            case RUN -> getNextStateAfterRun();
            case SLEEP -> isWakingUp() ? StateType.WAKE : StateType.SLEEP;
            case WAKE -> StateType.IDLE;
            case YAWN -> StateType.SLEEP;
            default -> {
                LOGGER.log(Level.WARNING, "Unknown state: {0}", oldState.name());
                yield StateType.IDLE;
            }
        };
        tomodachiModel.setCurrentAnimation(nextState);
        if (nextState != oldState) {
            LOGGER.log(Level.INFO, "Changed state from {0} to {1}", new Object[] { oldState.name(), nextState.name() });
        }
    }

    /**
     * Gets the next state after the idle animation finished. If the distance to the
     * next position is greater than {@link #DISTANCE_BEFORE_START_MOVING} the
     * tomodachi will start running.
     * There is a chance that the tomodachi will start yawning and then sleeping
     * else the tomodachi will continue idling.
     * 
     * @return The next state
     */
    private StateType getNextStateAfterIdle() {
        if (getDistanceToNextPosition() > DISTANCE_BEFORE_START_MOVING) {
            return StateType.RUN;
        }
        if (isYawning()) {
            return StateType.YAWN;
        }
        return StateType.IDLE;
    }

    /**
     * Gets the state after the run animation finished. The tomodachi will either
     * start yawning or sleep.
     */
    private StateType getNextStateAfterRun() {
        if (getDistanceToNextPosition() < DISTANCE_BEFORE_START_MOVING) {
            return StateType.IDLE;
        }
        if (isYawning()) {
            return StateType.YAWN;
        }
        return StateType.RUN;
    }

    /**
     * Calculates the distance between the tomodachi and the position returned by
     * {@link #nextPositionStrategy}
     * 
     * @return The distance to the next target position
     */
    private float getDistanceToNextPosition() {
        Vector2 nextPosition = getNextPosition();
        Vector2 current = tomodachiModel.getPosition();
        return nextPosition.subtract(current).absolute();
    }

    /**
     * If the tomodachi should wake up then the method returns true
     * 
     * @return If the tomodachi should wake up
     */
    private boolean isWakingUp() {
        return tomodachiModel.getSettings().getWakeChance() > random.nextFloat();
    }

    /**
     * If the tomodachi should yawn then the method returns true
     * 
     * @return If the tomodachi should yawn
     */
    private boolean isYawning() {
        return tomodachiModel.getSettings().getSleepChance() > random.nextFloat();
    }

    @Override
    public void reset() {
        // currently not needed in this implementation
    }

    /**
     * Gets the next position from the {@link #nextPositionStrategy}
     * 
     * @return the next target position
     */
    private Vector2 getNextPosition() {
        return getNextPositionStrategy().nextPosition(tomodachiModel);
    }

    /**
     * Creates a new {@link NextPositionStrategy} from the factory sets it to
     * {@link #nextPositionStrategy}
     */
    private void calculateNextPositionStrategy() {
        nextPositionStrategy = tomodachiModel.getSettings().getNextPositionStrategyFactory()
                .createNextPosition(environment);
        Objects.requireNonNull(nextPositionStrategy, "NextPositionStrategy must not be null");
    }

    /**
     * Gets the current next position strategy
     * 
     * @return the strategy
     */
    public NextPositionStrategy getNextPositionStrategy() {
        return nextPositionStrategy;
    }

}
