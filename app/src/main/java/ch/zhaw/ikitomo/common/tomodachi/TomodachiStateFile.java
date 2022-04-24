package ch.zhaw.ikitomo.common.tomodachi;

import java.util.Objects;

import ch.zhaw.ikitomo.common.StateType;

/**
 * A state of the tomodachi's state machine
 */
public class TomodachiStateFile {
    /**
     * The type of the state
     */
    private StateType type;
    /**
     * The animation beloging to the state
     */
    private String animation;

    /**
     * Constructor
     */
    public TomodachiStateFile() {
    }

    /**
     * Constructor
     * 
     * @param type      the name
     * @param animation the animation of the state
     */
    public TomodachiStateFile(StateType type, String animation) {
        this.type = type;
        this.animation = animation;
    }

    /**
     * @return the name
     */
    public StateType getType() {
        return type;
    }

    /**
     * @return the animation
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
        if (!(obj instanceof TomodachiStateFile)) {
            return false;
        }
        TomodachiStateFile other = (TomodachiStateFile) obj;
        return Objects.equals(animation, other.animation) && Objects.equals(type, other.type);
    }

}
