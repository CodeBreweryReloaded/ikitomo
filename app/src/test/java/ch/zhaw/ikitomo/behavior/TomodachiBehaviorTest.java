package ch.zhaw.ikitomo.behavior;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.random.RandomGenerator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ch.zhaw.ikitomo.common.Direction;
import ch.zhaw.ikitomo.common.StateType;
import ch.zhaw.ikitomo.common.Vector2;
import ch.zhaw.ikitomo.common.tomodachi.TomodachiSettings;

/**
 * Tests the {@link TomodachiBehavior}
 */
class TomodachiBehaviorTest {

    /**
     * A mocked {@link TomodachiSettings}
     */
    @Mock
    private TomodachiSettings tomodachiSettings;

    /**
     * A mocked {@link BehaviorModel}
     */
    @Mock
    private BehaviorModel behaviorModel;

    /**
     * A mocked random generator used in {@link #strategy}
     */
    @Mock
    private RandomGenerator random;

    /**
     * The {@link TomodachiBehavior} under test
     */
    private TomodachiBehavior strategy;

    /**
     * Sets up the basic behavior of the {@link TomodachiBehavior} instance
     */
    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        // setup tomodachiSettings
        when(tomodachiSettings.getSpeed()).thenReturn(1f);
        when(tomodachiSettings.getWakeChance()).thenReturn(1f);
        when(tomodachiSettings.getSleepChance()).thenReturn(1f);

        // setup behaviorModel
        when(behaviorModel.getSettings()).thenReturn(tomodachiSettings);

        // setup strategy object to mock
        strategy = new TomodachiBehavior(behaviorModel);
        strategy.setRandom(random);
    }

    /**
     * Tests that the {@link TomodachiBehavior#update(double)} does nothing for
     * SLEEP, YAWN and WAKE
     * 
     * @param input The input for test
     */
    @ParameterizedTest
    @EnumSource(value = StateType.class, names = { "SLEEP", "YAWN", "WAKE" })
    void testUpdateNOPStates(StateType input) {
        when(behaviorModel.getCurrentAnimationState()).thenReturn(input);
        strategy.update(1);
        verify(behaviorModel, never()).setCurrentAnimation(any(), any());
    }

    /**
     * Tests that the next position is correctly calculated when in the state RUN
     */
    @Test
    void testUpdateRun() {
        var oldPosition = new Vector2(100, 100);
        when(behaviorModel.getCurrentAnimationState()).thenReturn(StateType.RUN);
        when(behaviorModel.getPosition()).thenReturn(oldPosition);
        when(behaviorModel.getNextPosition()).thenReturn(Vector2.ZERO);
        strategy.update(2);
        verify(behaviorModel).setPosition(vectorThat(new Vector2(98.585786, 98.585786), 0.001));
        verify(behaviorModel).setCurrentAnimation(StateType.RUN, Direction.UP_LEFT);
    }

    /**
     * Tests that {@link TomodachiBehavior#update(double)} changes to the IDLE state
     * from the RUN state when the tomodachi is close enough to the mouse
     */
    @Test
    void testUpdateRunChangeToIdle() {
        var oldPosition = new Vector2(10, 10);
        when(behaviorModel.getCurrentAnimationState()).thenReturn(StateType.RUN);
        when(behaviorModel.getPosition()).thenReturn(oldPosition);
        when(behaviorModel.getNextPosition()).thenReturn(Vector2.ZERO);
        strategy.update(2);
        verify(behaviorModel, never()).setPosition(any());
        verify(behaviorModel).setCurrentAnimation(StateType.IDLE);
    }

    /**
     * Tests that the {@link TomodachiBehavior#update(double)} changes from idle to
     * run when the tomodachi is too far away from the mouse
     */
    @Test
    void testUpdateIdleToRun() {
        var oldPosition = new Vector2(200, 200);
        when(behaviorModel.getCurrentAnimationState()).thenReturn(StateType.IDLE);
        when(behaviorModel.getPosition()).thenReturn(oldPosition);
        when(behaviorModel.getNextPosition()).thenReturn(Vector2.ZERO);
        strategy.update(2);
        verify(behaviorModel).setCurrentAnimation(StateType.RUN, Direction.UP_LEFT);
    }

    /**
     * Tests that {@link TomodachiBehavior#animationFinished(StateType)} for yawn
     * and wake transitions to sleep and idle
     * 
     * @param input    The current state
     * @param expected The expected state
     */
    @ParameterizedTest
    @CsvSource({ "YAWN,SLEEP", "WAKE,IDLE" })
    void testAnimationFinishedSimpleStates(StateType input, StateType expected) {
        strategy.animationFinished(input);
        verify(behaviorModel).setCurrentAnimation(expected, Direction.NONE);
    }

    /**
     * Tests if {@link TomodachiBehavior#animationFinished(StateType)} changes from
     * sleep to wake when the random generator returns a number higher than the wake
     * chance
     * 
     * @param chance     The next float from the random generator
     * @param inputState The current state
     * @param expected   The expected state
     */
    @ParameterizedTest
    @CsvSource({ "0,SLEEP,WAKE", "1,SLEEP,SLEEP", "1.1,SLEEP,SLEEP" })
    void testAnimationFinishedSleep(float chance, StateType inputState, StateType expected) {
        when(random.nextFloat()).thenReturn(chance);
        strategy.animationFinished(inputState);
        verify(behaviorModel).setCurrentAnimation(expected, Direction.NONE);
    }

    /**
     * Tests if {@link TomodachiBehavior#animationFinished(StateType)} changes from
     * idle/run to yawn
     * 
     * 
     * @param chance     The next float from the random generator
     * @param inputState The current state
     * @param expected   The expected state
     */
    @ParameterizedTest
    @CsvSource({ "1,IDLE,IDLE", "0,IDLE,YAWN", "0,RUN,YAWN" })
    void testAnimationFinishedIdleOrRunToYawn(float chance, StateType inputState, StateType expected) {
        when(random.nextFloat()).thenReturn(chance);
        strategy.animationFinished(inputState);
        verify(behaviorModel).setCurrentAnimation(expected, Direction.NONE);
    }

    /**
     * Tests if {@link TomodachiBehavior#animationFinished(StateType)} when the
     * tomodachi doesn't yawn will stay in the run state and doesn't change the
     * direction
     */
    @Test
    void testAnimationFinishedRunToRun() {
        when(random.nextFloat()).thenReturn(1f);
        when(behaviorModel.getCurrentAnimationDirection()).thenReturn(Direction.DOWN);
        strategy.animationFinished(StateType.RUN);
        verify(behaviorModel).setCurrentAnimation(StateType.RUN, Direction.DOWN);
    }

    /**
     * Tests that {@link TomodachiBehavior#tomodachiWasClicked()} changes the state
     * to {@link StateType#EAT}
     */
    @Test
    void testTomodachiWasClicked() {
        strategy.tomodachiWasClicked();
        verify(behaviorModel).setCurrentAnimation(StateType.EAT);
    }

    /**
     * Makes sure that the given vector is within the given tolerance. This method
     * is used like {@link org.mockito.Mockito#argThat(org.mockito.ArgumentMatcher)}
     * 
     * @param expected  The expected vector
     * @param precision The precision
     * @return null
     */
    static Vector2 vectorThat(Vector2 expected, double precision) {
        return argThat(v -> v.equals(expected, precision));
    }

}
