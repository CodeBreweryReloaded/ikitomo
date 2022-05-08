package ch.zhaw.ikitomo.common;

import java.util.Arrays;

/**
 * Represents a vetor
 * 
 * @param x The x coordinate
 * @param y The y coordinate
 */
public record Vector2(float x, float y) {
    /**
     * The zero vector
     */
    public static final Vector2 ZERO = new Vector2(0, 0);

    /**
     * Adds the given vector to this vector and returns it
     * 
     * @param v The other vector
     * @return The newly calculated vector
     */
    public Vector2 add(Vector2 v) {
        return new Vector2(x + v.x, y + v.y);
    }

    /**
     * Subtracts the given vector from this vector and returns it
     * 
     * @param v The other vector
     * @return The newly calculated vector
     */
    public Vector2 subtract(Vector2 v) {
        return new Vector2(x - v.x, y - v.y);
    }

    /**
     * Multiplies the given scalar with this vector and returns it
     * 
     * @param n The scalar
     * @return The newly calculated vector
     */
    public Vector2 multiply(float n) {
        return new Vector2(x * n, y * n);
    }

    /**
     * Inverts the vector by multiplying it with -1
     *
     * @return The inverse of the vector
     * @see #multiply(int)
     */
    public Vector2 inverse() {
        return multiply(-1);
    }

    /**
     * Divides this vector by the given scalar and returns it
     * 
     * @param n The scalar
     * @return The newly calculated vector
     */
    public Vector2 divide(float n) {
        return new Vector2(x / n, y / n);
    }

    /**
     * Returns the normalized vector
     * 
     * @return the normalized vector with a length of approximately 1
     */
    public Vector2 normalize() {
        int length = (int) absolute();
        if (length == 0) {
            return ZERO;
        }
        return divide(length);
    }

    /**
     * Gets the length of this vector
     *
     * @return The length of this vector
     */
    public float absolute() {
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * Returns the direction of this vector
     * 
     * @return the direction of this vector
     */
    public Direction direction() {
        Vector2 norm = normalize();
        return Arrays.stream(Direction.values()).filter(d -> d.getVector().equals(norm)).findAny()
                .orElse(Direction.NONE);
    }
}
