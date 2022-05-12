package ch.zhaw.ikitomo.overlay.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ch.zhaw.ikitomo.common.Direction;
import ch.zhaw.ikitomo.common.StateType;
import ch.zhaw.ikitomo.overlay.model.animation.AnimationData;
import ch.zhaw.ikitomo.overlay.model.animation.Cell;
import ch.zhaw.ikitomo.overlay.model.animation.Frame;
import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

/**
 * Tests the {@link SpritesheetAnimator}
 */
class SpritesheetAnimatorTest {

    /**
     * The mocked animation Timer
     */
    @Mock
    AnimationTimer animationTimer;

    /**
     * The mocked animation map
     */
    ObservableMap<StateType, List<AnimationData>> animations;

    /**
     * The animator instance
     */
    SpritesheetAnimator animator;

    /**
     * Setup function that is run before each test
     */
    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        // Setup mocks
        doNothing().when(animationTimer).start();
        doNothing().when(animationTimer).stop();

        animations = FXCollections.observableMap(new EnumMap<>(StateType.class));
        Frame frame = new Frame(new Cell(8, 8, 8, 8), 1);
        animations.put(StateType.RUN,
                Arrays.asList(new AnimationData(new Frame[] { frame, frame }, null, Direction.UP)));

        animator = new SpritesheetAnimator(animations);
        animator.setAnimationTimer(animationTimer);
    }

    /**
     * Tests if the animator correctly chooses a fallback animation when the
     * preferred one doesn't exist
     */
    @Test
    void testFallbackAnimation() {
        animator.setAnimation(StateType.YAWN, Direction.NONE);
        assertEquals(StateType.IDLE, animator.getCurrentState());
        assertEquals(Direction.NONE, animator.getCurrentDirection());
    }

    /**
     * Tests if the animator correctly grabs the requested animation
     */
    @Test
    void testAnimationChange() {
        animator.setAnimation(StateType.RUN, Direction.UP);
        assertEquals(StateType.RUN, animator.getCurrentState());
        assertEquals(Direction.UP, animator.getCurrentDirection());
    }

    /**
     * Tests if the animator correctly advances frames
     */
    @Test
    void testUpdate() {
        animator.update(Long.MAX_VALUE);
        assertEquals(1, animator.getCurrentFrameID());
    }
}
