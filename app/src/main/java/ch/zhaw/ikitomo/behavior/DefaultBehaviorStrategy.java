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

public class DefaultBehaviorStrategy implements BehaviorStrategy {
    private static final float DISTANCE_BEFORE_START_MOVING = 200;

    private static final Logger LOGGER = Logger.getLogger(DefaultBehaviorStrategy.class.getName());

    private Random random = new Random();

    private TomodachiEnvironment environment;

    private TomodachiModel tomodachiModel;

    private NextPositionStrategy nextPositionStrategy;

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

    private StateType getNextStateAfterIdle() {
        if (getDistanceToNextPosition() > DISTANCE_BEFORE_START_MOVING) {
            return StateType.RUN;
        }
        if (isYawning()) {
            return StateType.YAWN;
        }
        return StateType.IDLE;
    }

    private StateType getNextStateAfterRun() {
        if (getDistanceToNextPosition() < DISTANCE_BEFORE_START_MOVING) {
            return StateType.IDLE;
        }
        if (isYawning()) {
            return StateType.YAWN;
        }
        return StateType.RUN;
    }

    private float getDistanceToNextPosition() {
        Vector2 nextPosition = getNextPosition();
        Vector2 current = tomodachiModel.getPosition();
        return nextPosition.subtract(current).absolute();
    }

    private boolean isWakingUp() {
        return tomodachiModel.getSettings().getWakeChance() > random.nextFloat();
    }

    private boolean isYawning() {
        return tomodachiModel.getSettings().getSleepChance() > random.nextFloat();
    }

    @Override
    public void reset() {

    }

    private Vector2 getNextPosition() {
        return getNextPositionStrategy().nextPosition(tomodachiModel);
    }

    private void calculateNextPositionStrategy() {
        nextPositionStrategy = tomodachiModel.getSettings().getNextPositionStrategyFactory()
                .createNextPosition(environment);
        Objects.requireNonNull(nextPositionStrategy, "NextPositionStrategy must not be null");
    }

    public NextPositionStrategy getNextPositionStrategy() {
        return nextPositionStrategy;
    }

}
