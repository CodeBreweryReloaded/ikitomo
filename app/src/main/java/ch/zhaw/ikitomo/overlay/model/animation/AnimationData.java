package ch.zhaw.ikitomo.overlay.model.animation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ch.zhaw.ikitomo.common.Direction;
import javafx.scene.image.Image;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AnimationData {
    private Frame[] frames;

    @JsonIgnore
    private Image image;

    @JsonIgnore
    private Direction direction;

    private AnimationData() {

    }

    public Frame[] getFrames() {
        return frames;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Image getImage() {
        return image;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}