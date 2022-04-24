package ch.zhaw.ikitomo.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

/**
 * Tests the Vector class
 */
public class Vector2Test {
    /**
     * Tests if {@link Vector2#add(Vector2)} works correctly
     */
    @Test
    public void testAddition() {
        var v1 = new Vector2(1, 1);
        var v2 = new Vector2(2, 2);
        assertEquals(new Vector2(3, 3), v1.add(v2));
    }

    /**
     * Tests if {@link Vector2#subtract(Vector2)} works correctly
     */
    @Test
    public void testSubtraction() {
        var v1 = new Vector2(1, 1);
        var v2 = new Vector2(2, 2);
        assertEquals(new Vector2(-1, -1), v1.subtract(v2));
    }

    /**
     * Tests if {@link Vector2#multiply(int)} works correctly
     */
    @Test
    public void testMultiplication() {
        var v1 = new Vector2(1, 1);
        assertEquals(new Vector2(2, 2), v1.multiply(2));
    }

    /**
     * Tests if {@link Vector2#inverse()} works correctly
     */
    @Test
    public void testInverse() {
        var v1 = new Vector2(1, 1);
        assertEquals(new Vector2(-1, -1), v1.inverse());
    }

    /**
     * Tests if {@link Vector2#divide(int)} works correctly
     */
    @Test
    public void testDivide() {
        var v1 = new Vector2(2, 2);
        assertEquals(new Vector2(1, 1), v1.divide(2));

        // the coordinates will be rounded down
        var v2 = new Vector2(1, 1);
        assertEquals(new Vector2(0, 0), v2.divide(2));
    }

    /**
     * Tests if {@link Vector2#absolute()} works correctly
     */
    @Test
    public void testAbsolute() {
        var v1 = new Vector2(2, 2);
        assertEquals(2.828427, v1.absolute(), 0.00001);

    }

    /**
     * Tests if {@link Vector2#equals(Object)} works correctly
     */
    @Test
    public void testEquals() {
        assertEquals(new Vector2(1, 1), new Vector2(1, 1));
        assertNotEquals(new Vector2(1, 1), new Vector2(2, 2));
    }
}
