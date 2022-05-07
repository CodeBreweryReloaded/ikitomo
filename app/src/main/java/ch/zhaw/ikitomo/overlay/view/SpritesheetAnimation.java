package ch.zhaw.ikitomo.overlay.view;

import java.util.EnumMap;
import java.util.HashMap;
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
import javafx.beans.property.SimpleObjectProperty;
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
    private ObjectProperty<Image> imageProperty = new SimpleObjectProperty<>();

    /**
     * Bindable property containing the current cell on the spritesheet
     */
    private ObjectProperty<Rectangle2D> cellProperty = new SimpleObjectProperty<>();

    /**
     * A binding that binds to the currently loaded animations
     */
    private Map<StateType, List<AnimationData>> animations = new EnumMap<>(StateType.class);

    private AnimationData currentAnimation;

    private long last;

    private int currentFrameID = 0;

    private long frameDuration = 0;

    private long lastFrame;

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
        if (changeAnimation) {
            imageProperty.set(currentAnimation.getImage());
            changeAnimation = false;
            currentFrameID = 0;
        }

        Frame currentFrame = currentAnimation.getFrames()[currentFrameID];

        if (currentFrame.duration() * 1_000_000 + lastFrame < now) {
            lastFrame = now;
            currentFrameID = (currentFrameID + 1) % currentAnimation.getFrames().length;

            currentFrame = currentAnimation.getFrames()[currentFrameID];
            cellProperty.set(new Rectangle2D(
                    currentFrame.cell().positionX(),
                    currentFrame.cell().positionY(),
                    currentFrame.cell().width(),
                    currentFrame.cell().height()));
        }

    }

    public ObjectProperty<Image> getImageProperty() {
        return imageProperty;
    }

    public ObjectProperty<Rectangle2D> getCellProperty() {
        return cellProperty;
    }

}
