package ch.zhaw.ikitomo.behavior;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.random.RandomGenerator;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ch.zhaw.ikitomo.common.Direction;
import ch.zhaw.ikitomo.common.StateType;
import ch.zhaw.ikitomo.common.Vector2;
import ch.zhaw.ikitomo.common.tomodachi.TomodachiSettings;

class DefaultBehaviorStrategyTest {

    @Mock
    private TomodachiSettings tomodachiSettings;

    @Mock
    private BehaviorModel behaviorModel;

    @Mock
    private RandomGenerator random;

    private TomodachiBehavior strategy;

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

    @ParameterizedTest
    @EnumSource(value = StateType.class, names = { "IDLE", "SLEEP", "YAWN", "WAKE" })
    void testUpdateNOPStates(StateType input) {
        when(behaviorModel.getCurrentAnimationState()).thenReturn(input);
        strategy.update(1);
        verify(behaviorModel, never()).setCurrentAnimation(any(), any());
    }

    @Test
    void testUpdateRun() {
        var oldPosition = new Vector2(100, 100);
        when(behaviorModel.getCurrentAnimationState()).thenReturn(StateType.RUN);
        when(behaviorModel.getPosition()).thenReturn(oldPosition);
        when(behaviorModel.getNextPosition()).thenReturn(Vector2.ZERO);
        strategy.update(2);
        verify(behaviorModel).setPosition(vectorThat(new Vector2(98.585786, 98.585786), 0.001));
        verify(behaviorModel).setCurrentAnimationDirection(Direction.UP_LEFT);
    }

    @ParameterizedTest
    @CsvSource({ "YAWN,SLEEP", "WAKE,IDLE" })
    void testAnimationFinishedSimpleStates(StateType input, StateType expected) {
        strategy.animationFinished(input);
        verify(behaviorModel).setCurrentAnimation(expected);
    }

    @ParameterizedTest
    @CsvSource({ "0,SLEEP,WAKE", "1,SLEEP,SLEEP", "1.1,SLEEP,SLEEP" })
    void testAnimationFinishedSleep(float chance, StateType inputState, StateType expected) {
        when(random.nextFloat()).thenReturn(chance);
        strategy.animationFinished(inputState);
        verify(behaviorModel).setCurrentAnimation(expected);
    }

    @ParameterizedTest
    @MethodSource
    void testAnimationFinishedIdle(TestAnimationFinishedInput input) {
        when(random.nextFloat()).thenReturn(input.nextRandomFloat());
        when(behaviorModel.getPosition()).thenReturn(Vector2.ZERO);
        when(behaviorModel.getNextPosition()).thenReturn(input.nextPos());

        strategy.animationFinished(input.inputState());
        verify(behaviorModel).setCurrentAnimation(input.expectedState());
    }

    private record TestAnimationFinishedInput(float nextRandomFloat, Vector2 nextPos, StateType inputState,
            StateType expectedState) {
    }

    static Stream<TestAnimationFinishedInput> testAnimationFinishedIdle() {
        return Stream.of(
                // test inputs for idle state
                new TestAnimationFinishedInput(1, Vector2.ZERO, StateType.IDLE, StateType.IDLE),
                new TestAnimationFinishedInput(0, Vector2.ZERO, StateType.IDLE, StateType.YAWN),
                new TestAnimationFinishedInput(0, new Vector2(1000, 1000), StateType.IDLE, StateType.RUN),
                // test inputs for run state
                new TestAnimationFinishedInput(1, new Vector2(1000, 1000), StateType.RUN, StateType.RUN),
                new TestAnimationFinishedInput(0, new Vector2(1000, 1000), StateType.RUN, StateType.YAWN),
                new TestAnimationFinishedInput(0, Vector2.ZERO, StateType.RUN, StateType.IDLE));
    }

    static Vector2 vectorThat(Vector2 expected, double precision) {
        return argThat(v -> v.x() < expected.x() + precision && v.x() > expected.x() - precision
                && v.y() < expected.y() + precision && v.y() > expected.y() - precision);
    }

}
