package ch.zhaw.ikitomo.overlay.view;

import java.util.List;

import ch.zhaw.ikitomo.common.StateType;
import ch.zhaw.ikitomo.overlay.OverlayController;
import ch.zhaw.ikitomo.overlay.model.animation.AnimationData;
import javafx.collections.ObservableMap;
import javafx.scene.image.ImageView;

/**
 * This class works similarly to a regular {@link ImageView} except that its
 * main purpose is to display an animated sequence of images instead of just
 * one. Notably, animations of this class are not set via {@link #setImage} or
 * binding an image property. Instead, a list of animations mapped to states and
 * directions are provided to the view (and the animator). The
 * {@link SpritesheetAnimator} contains functions to change the current
 * animation by looking it up with the state.
 */
public class AnimatedImageView extends ImageView {

    /**
     * Holds the animator
     */
    private SpritesheetAnimator animator;

    /**
     * Constructor
     * @param animations The observable map of animations
     */
    public AnimatedImageView(ObservableMap<StateType, List<AnimationData>> animations) {
        setStyle(OverlayController.TRANSPARENT_STYLE);
        this.animator = new SpritesheetAnimator(animations);
        imageProperty().bind(animator.imageProperty());
        viewportProperty().bind(animator.viewportProperty());
    }

    /**
     * Gets the active {@link SpritesheetAnimator} instanve
     * @return The animator
     */
    public SpritesheetAnimator getAnimator() {
        return animator;
    }
}
