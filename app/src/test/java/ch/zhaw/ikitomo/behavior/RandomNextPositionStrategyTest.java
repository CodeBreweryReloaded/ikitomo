package ch.zhaw.ikitomo.behavior;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.when;

import java.util.random.RandomGenerator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ch.zhaw.ikitomo.common.Vector2;
import ch.zhaw.ikitomo.overlay.model.TomodachiModel;
import javafx.geometry.Rectangle2D;

/**
 * Tests {@link RandomNextPositionStrategy}
 */
class RandomNextPositionStrategyTest {
    /**
     * The tomodachi model
     */
    @Mock
    private TomodachiModel tomodachiModel;

    /**
     * The random generator
     */
    @Mock
    private RandomGenerator random;

    /**
     * The {@link RandomNextPositionStrategy} to test
     */
    private RandomNextPositionStrategy strategy;

    /**
     * The screen bounds
     */
    private Rectangle2D screenBounds = new Rectangle2D(10, 10, 1000, 1000);

    /**
     * Setup mocks and objects
     */
    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        strategy = new RandomNextPositionStrategy();
        strategy.setRandom(random);
        strategy.setScreenBoundsSupplier(() -> screenBounds);
        when(tomodachiModel.getPosition()).thenReturn(new Vector2(500, 500));
    }

    /**
     * Tests if the same next position is returned if the tomodachi is too far away
     * from the last position
     */
    @Test
    void testNextPositionWhenTomodachiIsFar() {
        var expectedPos = new Vector2(70, 70);
        when(random.nextDouble(anyDouble(), anyDouble())).thenReturn(expectedPos.x(), expectedPos.y(), 100d);
        Vector2 nextPos1 = strategy.nextPosition(tomodachiModel);
        assertEquals(expectedPos, nextPos1);

        // has to return the same position until the tomodachi is closer
        Vector2 nextPos2 = strategy.nextPosition(tomodachiModel);
        assertEquals(expectedPos, nextPos2);
    }

    /**
     * Tests if a new next position is returned if the tomodachi is close enough to
     * the last position
     */
    @Test
    void testNextPositionWhenTomodachiIsClose() {
        var expectedPos1 = new Vector2(70, 70);
        var expectedPos2 = new Vector2(30, 30);
        when(random.nextDouble(anyDouble(), anyDouble())).thenReturn(expectedPos1.x(), expectedPos1.y(),
                expectedPos2.x(), expectedPos2.y(), 100d);
        Vector2 nextPos1 = strategy.nextPosition(tomodachiModel);
        assertEquals(expectedPos1, nextPos1);

        // has to return the same position until the tomodachi is closer
        when(tomodachiModel.getPosition()).thenReturn(new Vector2(60, 60));
        Vector2 nextPos2 = strategy.nextPosition(tomodachiModel);
        assertEquals(expectedPos2, nextPos2);
    }

    /**
     * Tests if a new next position is returned if the position is out of bounds
     */
    @Test
    void testNextPositionWhenTomodachiWhenPositionIsOutOfBounds() {
        var expectedPos1 = new Vector2(70, 70);
        var expectedPos2 = new Vector2(30, 30);
        when(random.nextDouble(anyDouble(), anyDouble())).thenReturn(expectedPos1.x(), expectedPos1.y(),
                expectedPos2.x(), expectedPos2.y(), 100d);
        Vector2 nextPos1 = strategy.nextPosition(tomodachiModel);
        assertEquals(expectedPos1, nextPos1);

        // has to return the same position until the tomodachi is closer
        screenBounds = new Rectangle2D(0, 0, 10, 10);
        Vector2 nextPos2 = strategy.nextPosition(tomodachiModel);
        assertEquals(expectedPos2, nextPos2);
    }
}
