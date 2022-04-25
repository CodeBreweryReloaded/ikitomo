package ch.zhaw.ikitomo.common;

/**
 * Represents a vetor
 * 
 * @param x the x coordinate
 * @param y the y coordinate
 */
public record Vector2(int x, int y) {
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
    public Vector2 multiply(int n) {
        return new Vector2(x * n, y * n);
    }

    /**
     * This vector is multiplied with -1 and returned
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
    public Vector2 divide(int n) {
        return new Vector2(x / n, y / n);
    }

    /**
     * @return The length of this vector
     */
    public float absolute() {
        return (float) Math.sqrt(x * x + y * y);
    }
}
