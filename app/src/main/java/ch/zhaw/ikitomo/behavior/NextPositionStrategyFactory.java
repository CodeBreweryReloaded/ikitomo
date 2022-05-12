package ch.zhaw.ikitomo.behavior;

import java.util.function.Supplier;

/**
 * An enum with all possible factory methods for {@link NextPositionStrategy}s.
 * For this an enum is used to allow for serialization via Jackson
 */
public enum NextPositionStrategyFactory {
    /**
     * The factory to create a {@link NextPositionStrategy} that follows the mouse
     */
    FOLLOW_MOUSE("Follow the Mouse", MouseFollowStrategy::new),
    /**
     * The factory to create a {@link NextPositionStrategy} that returns a random
     * position. The same position is returned until the Tomodachi walks into a
     * radius around the last returned position
     */
    RANDOM_POSITION("Random Position", RandomNextPositionStrategy::new);

    /**
     * The name shown in the ui
     */
    private String name;

    /**
     * The supplier which supplies the {@link NextPositionStrategy} instance
     */
    private Supplier<NextPositionStrategy> factory;

    /**
     * Constructor
     * 
     * @param factory The factory method creating the {@link NextPositionStrategy}
     *                object
     */
    private NextPositionStrategyFactory(String name, Supplier<NextPositionStrategy> factory) {
        this.name = name;
        this.factory = factory;
    }

    /**
     * Gets the name shown in the ui
     * 
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * Creates a new {@link NextPositionStrategy} with this factory
     * 
     * @param environment the environment
     * @return the newly created {@link NextPositionStrategy} object
     */
    public NextPositionStrategy createNextPosition() {
        return factory.get();
    }
}
