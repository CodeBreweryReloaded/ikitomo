package ch.zhaw.ikitomo.common;

import javafx.scene.image.Image;

/**
 * Represents an animation
 * 
 * @param duration The duration of each frame
 * @param image    The image of the animation
 */
public record Animation(int duration, Image image) {
}
