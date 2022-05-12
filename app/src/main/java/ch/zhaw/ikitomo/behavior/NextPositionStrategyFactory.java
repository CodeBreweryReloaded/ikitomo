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
    FOLLOW_MOUSE(MouseFollowStrategy::new);

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
    private NextPositionStrategyFactory(Supplier<NextPositionStrategy> factory) {
        this.factory = factory;
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