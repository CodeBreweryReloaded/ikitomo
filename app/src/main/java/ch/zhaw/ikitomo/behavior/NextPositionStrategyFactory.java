package ch.zhaw.ikitomo.behavior;

import java.util.function.Function;

import ch.zhaw.ikitomo.common.tomodachi.TomodachiEnvironment;

/**
 * An enum with all possible factory methods for {@link NextPositionStrategy}s.
 * For this an enum is used to allow for serialization via Jackson
 */
public enum NextPositionStrategyFactory {
    FOLLOW_MOUSE(env -> new MouseFollowStrategy());

    private Function<TomodachiEnvironment, NextPositionStrategy> factory;

    /**
     * Constructor
     * 
     * @param factory The factory method creating the {@link NextPositionStrategy}
     *                object
     */
    private NextPositionStrategyFactory(Function<TomodachiEnvironment, NextPositionStrategy> factory) {
        this.factory = factory;
    }

    /**
     * Creates a new {@link NextPositionStrategy} with this factory
     * 
     * @param environment the environment
     * @return the newly created {@link NextPositionStrategy} object
     */
    public NextPositionStrategy createNextPosition(TomodachiEnvironment environment) {
        return factory.apply(environment);
    }
}
