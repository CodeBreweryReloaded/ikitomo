package ch.zhaw.ikitomo.common;

/**
 * Represents nine direction for use with animations
 */
public enum Direction {
    /**
     * No direction/neutral
     */
    NONE(Vector2.ZERO),
    /**
     * North
     */
    UP(new Vector2(0, -1)),
    /**
     * North-East
     */
    UP_RIGHT(new Vector2(1, -1)),
    /**
     * East
     */
    RIGHT(new Vector2(1, 0)),
    /**
     * South-East
     */
    DOWN_RIGHT(new Vector2(1, 1)),
    /**
     * South
     */
    DOWN(new Vector2(0, 1)),
    /**
     * South-West
     */
    DOWN_LEFT(new Vector2(-1, 1)),
    /**
     * West
     */
    LEFT(new Vector2(-1, 0)),
    /**
     * North-West
     */
    UP_LEFT(new Vector2(-1, -1));

    /**
     * The vector associated with this direction
     */
    private Vector2 vector;

    /**
     * Constructor
     * 
     * @param vector the directional vector of this direction
     */
    private Direction(Vector2 vector) {
        this.vector = vector.normalize();
    }

    /**
     * Gets the normalized direction vector of this direction
     * 
     * @return the direction vector
     */
    public Vector2 getVector() {
        return vector;
    }

}
