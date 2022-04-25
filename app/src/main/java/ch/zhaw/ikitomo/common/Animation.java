package ch.zhaw.ikitomo.common;

import javafx.scene.image.Image;

/**
 * Represents an animation
 * 
 * @param duration The duration of each frame
 * @param image    The image of the animation
 */
public record Animation(int duration, Image image) {

    /**
     * Loads the animation
     * 
     * @param path     the path to the image
     * @param duration the duration
     * @return the animation
     */
    public static Animation load(String path, int duration) {
        // TODO: implement it. If many parameters are needed, consider using the builder
        // pattern
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
