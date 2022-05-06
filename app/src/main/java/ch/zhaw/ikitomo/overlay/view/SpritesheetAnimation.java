package ch.zhaw.ikitomo.overlay.view;

import java.util.List;
import java.util.Map;

import ch.zhaw.ikitomo.common.StateType;
import ch.zhaw.ikitomo.overlay.model.animation.AnimationData;
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

    /**
     * Constructor
     * @param animations An observable map containing {@link AnimationData}
     */
    public SpritesheetAnimation(ObservableMap<StateType, List<AnimationData>> animations) {
        Bindings.bindContent(this.animations, animations);
    }

    @Override
    public void handle(long now) {
        // TODO Auto-generated method stub

    }
}
