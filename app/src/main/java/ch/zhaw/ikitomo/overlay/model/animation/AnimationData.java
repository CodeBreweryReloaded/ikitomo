package ch.zhaw.ikitomo.overlay.model.animation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ch.zhaw.ikitomo.common.Direction;
import javafx.scene.image.Image;

/**
 * A simple data class to store all relevant information for an animation
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnimationData {
    /**
     * An array containing all animation frames
     */
    private Frame[] frames;

    /**
     * The spritesheet
     * <p>Note: Cell size can be determined from any element inside <code>frames[]</code>
     */
    @JsonIgnore
    private Image image;

    /**
     * Specifies the direction for directionally different animations
     */
    @JsonIgnore
    private Direction direction;

    /**
     * Private constructor for Jackson
     */
    private AnimationData() {

    }

    /**
     * Gets all animation frames
     * @return All animation frames
     */
    public Frame[] getFrames() {
        return frames;
    }

    /**
     * Sets the spritesheet
     * @param image The spritesheet
     */
    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * Gets the current spritesheet
     * @return The spritesheet
     */
    public Image getImage() {
        return image;
    }

    /**
     * Gets the current directional value
     * @return The direction
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Sets the animation's direction
     * @param direction
     */
    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}