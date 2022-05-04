package ch.zhaw.ikitomo.common.tomodachi;

import java.util.Objects;

import ch.zhaw.ikitomo.common.StateType;

/**
 * Represents a state of a tomodachi's state machine
 */
public class TomodachiStateDefinition {
    /**
     * The type of the state
     */
    private StateType type;
    /**
     * The animation belonging to the state
     */
    private String animation;

    /**
     * Constructor
     */
    public TomodachiStateDefinition() {
    }

    /**
     * Constructor
     * 
     * @param type      The name
     * @param animation The animation of the state
     */
    public TomodachiStateDefinition(StateType type, String animation) {
        this.type = type;
        this.animation = animation;
    }

    /**
     * Gets the type of the state
     *
     * @return The type of the state
     */
    public StateType getType() {
        return type;
    }

    /**
     * Gets the animation
     *
     * @return The animation
     */
    public String getAnimation() {
        return animation;
    }

    @Override
    public int hashCode() {
        return Objects.hash(animation, type);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof TomodachiStateDefinition)) {
            return false;
        }
        TomodachiStateDefinition other = (TomodachiStateDefinition) obj;
        return Objects.equals(animation, other.animation) && Objects.equals(type, other.type);
    }

}
