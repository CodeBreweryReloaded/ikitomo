package ch.zhaw.ikitomo.overlay.view;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ch.zhaw.ikitomo.common.StateType;
import ch.zhaw.ikitomo.overlay.model.animation.AnimationData;
import javafx.collections.ObservableMap;

/**
 * Tests the {@link SpritesheetAnimator}
 */
public class SpritesheetAnimatorTest {
    
    /**
     * The mocked animation map
     */
    @Mock
    ObservableMap<StateType, List<AnimationData>> animations;

    /**
     * A mocked animation
     */
    @Mock
    AnimationData fakeAnimation;

    /**
     * Setup function that is run before each test
     */
    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        // Setup mocks
        when(animations.values()).thenReturn(Arrays.asList(Arrays.asList(fakeAnimation)));
    }
}
