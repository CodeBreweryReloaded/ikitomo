package ch.zhaw.ikitomo.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests the Vector class
 */
class Vector2Test {
    /**
     * Tests if {@link Vector2#add(Vector2)} works correctly
     */
    @Test
    void testAddition() {
        var v1 = new Vector2(1, 1);
        var v2 = new Vector2(2, 2);
        assertEquals(new Vector2(3, 3), v1.add(v2));
    }

    /**
     * Tests if {@link Vector2#subtract(Vector2)} works correctly
     */
    @Test
    void testSubtraction() {
        var v1 = new Vector2(1, 1);
        var v2 = new Vector2(2, 2);
        assertEquals(new Vector2(-1, -1), v1.subtract(v2));
    }

    /**
     * Tests if {@link Vector2#multiply(int)} works correctly
     */
    @Test
    void testMultiplication() {
        var v1 = new Vector2(1, 1);
        assertEquals(new Vector2(2, 2), v1.multiply(2));
    }

    /**
     * Tests if {@link Vector2#inverse()} works correctly
     */
    @Test
    void testInverse() {
        var v1 = new Vector2(1, 1);
        assertEquals(new Vector2(-1, -1), v1.inverse());
    }

    /**
     * Tests if {@link Vector2#divide(int)} works correctly
     */
    @Test
    void testDivide() {
        var v1 = new Vector2(2, 2);
        assertEquals(new Vector2(1, 1), v1.divide(2));

        // the coordinates will be rounded down
        var v2 = new Vector2(1, 1);
        assertEquals(new Vector2(0.5f, 0.5f), v2.divide(2));
    }

    /**
     * Tests if {@link Vector2#absolute()} works correctly
     */
    @Test
    void testAbsolute() {
        var v1 = new Vector2(2, 2);
        assertEquals(2.828427, v1.absolute(), 0.00001);

    }

    /**
     * Tests if {@link Vector2#equals(Object)} works correctly
     */
    @Test
    void testEquals() {
        assertEquals(new Vector2(1, 1), new Vector2(1, 1));
        assertNotEquals(new Vector2(1, 1), new Vector2(2, 2));
    }

    /**
     * Tests if {@link Vector2#equals(Vector2, double)} works correctly
     */
    @Test
    void testEqualsWithPrecision() {
        assertTrue(new Vector2(1.001, 1.001).equals(new Vector2(1, 1), 0.01));
        assertFalse(new Vector2(1.001, 1.001).equals(new Vector2(1, 1), 0.001));
        assertFalse(new Vector2(1.001, 1.001).equals(new Vector2(1, 1), 0.0001));
    }

    /**
     * Tests if {@link Vector2#direction()}
     * 
     * @param input The input parameter
     */
    @ParameterizedTest
    @MethodSource
    void testDirection(TestDirectionInput input) {
        assertEquals(input.expectedDirection(), input.input().direction());
    }

    /**
     * The parameter for {@link Vector2Test#testDirection(TestDirectionInput)}
     */
    private record TestDirectionInput(Vector2 input, Direction expectedDirection) {
    }

    /**
     * The test parameters for {@link Vector2Test#testDirection(TestDirectionInput)}
     * 
     * @return The parameters
     */
    static Stream<TestDirectionInput> testDirection() {
        return Stream.of(
                new TestDirectionInput(Vector2.ZERO, Direction.NONE),
                new TestDirectionInput(new Vector2(1, 0), Direction.RIGHT),
                new TestDirectionInput(new Vector2(-1, 0), Direction.LEFT),
                new TestDirectionInput(new Vector2(0, 1), Direction.DOWN),
                new TestDirectionInput(new Vector2(0, -1), Direction.UP),
                new TestDirectionInput(new Vector2(1.1, 0.5), Direction.DOWN_RIGHT),
                new TestDirectionInput(new Vector2(1.1, -1.5), Direction.UP_RIGHT),
                new TestDirectionInput(new Vector2(-0.7, 1.5), Direction.DOWN_LEFT),
                new TestDirectionInput(new Vector2(-0.7, -1.6), Direction.UP_LEFT));
    }
}
