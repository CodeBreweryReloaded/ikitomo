package ch.zhaw.ikitomo.overlay.view;

import java.util.List;
import java.util.Map;

import com.google.common.util.concurrent.Service.State;

import ch.zhaw.ikitomo.common.Animation;
import ch.zhaw.ikitomo.common.StateType;
import ch.zhaw.ikitomo.overlay.model.animation.AnimationData;
import ch.zhaw.ikitomo.overlay.model.animation.Frame;
import javafx.animation.AnimationTimer;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableMap;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;

/**
 * This class provides the basic logic to display animations based on the
 * information specified in provided {@link AnimationData} objects. It also
 * provides an observable {@link Image} property and {@link Rectangle2D}
 * property that can be bound to a GUI element
 */
public class SpritesheetAnimation extends AnimationTimer {
    /**
     * Bindable property containing the current spritesheet
     */
    private ObjectProperty<Image> imageProperty;

    /**
     * Bindable property containing the current cell on the spritesheet
     */
    private ObjectProperty<Rectangle2D> cellProperty;

    /**
     * A binding that binds to the currently loaded animations
     */
    private Map<StateType, List<AnimationData>> animations;

    private AnimationData currentAnimation;

    private long last;

    private Frame currentFrame;

    private int currentFrameID = 0;

    private long delta;

    private long frameDuration = 0;

    private boolean changeAnimation = true;

    /**
     * Constructor
     * 
     * @param animations An observable map containing {@link AnimationData}
     */
    public SpritesheetAnimation(ObservableMap<StateType, List<AnimationData>> animations) {
        Bindings.bindContent(this.animations, animations);

        currentAnimation = animations.get(StateType.RUN).get(0);

    }

    @Override
    public void handle(long now) {
        delta = now - last;
        frameDuration += delta;
        if (changeAnimation) {
            imageProperty.set(currentAnimation.getImage());
        }

        currentFrame = currentAnimation.getFrames()[currentFrameID];

        if (currentFrame.duration() < frameDuration) {
            currentFrameID++;
            frameDuration -= currentFrame.duration();
            currentFrame = currentAnimation.getFrames()[currentFrameID];
            cellProperty.set(new Rectangle2D(
                    currentFrame.cell().positionX(),
                    currentFrame.cell().positionY(),
                    currentFrame.cell().width(),
                    currentFrame.cell().height()));
        }

        last = now;
    }

    public ObjectProperty<Image> getImageProperty() {
        return imageProperty;
    }

    public ObjectProperty<Rectangle2D> getCellProperty() {
        return cellProperty;
    }

}
