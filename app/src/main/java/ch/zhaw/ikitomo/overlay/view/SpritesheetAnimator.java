package ch.zhaw.ikitomo.overlay.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.logging.Logger;

import ch.zhaw.ikitomo.common.Direction;
import ch.zhaw.ikitomo.common.StateType;
import ch.zhaw.ikitomo.overlay.model.animation.AnimationData;
import ch.zhaw.ikitomo.overlay.model.animation.Frame;
import javafx.animation.AnimationTimer;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableMap;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;

/**
 * This class provides the basic logic to display animations based on the
 * information specified in provided {@link AnimationData} objects. It also
 * provides an observable {@link Image} property and {@link Rectangle2D}
 * property that can be bound to a GUI element
 */
public class SpritesheetAnimator {
    /**
     * Logger
     */
    private static final Logger LOGGER = Logger.getLogger(SpritesheetAnimator.class.getName());

    /**
     * Bindable property containing the current spritesheet
     */
    private ReadOnlyObjectWrapper<Image> imageProperty = new ReadOnlyObjectWrapper<>();

    /**
     * Bindable property containing the current cell on the spritesheet
     */
    private ReadOnlyObjectWrapper<Rectangle2D> cellProperty = new ReadOnlyObjectWrapper<>();

    /**
     * A binding that binds to the currently loaded animations
     */
    private Map<StateType, List<AnimationData>> animations = new EnumMap<>(StateType.class);

    /**
     * The currently playing animation
     */
    private AnimationData currentAnimation;

    /**
     * The default animation. This will always be the first idle animation
     */
    private AnimationData defaultAnimation;

    /**
     * Which cell is currently being displayed
     */
    private int currentFrameID = 0;

    /**
     * The timestamp of the last frame
     */
    private long lastFrame;

    /**
     * If the animation has to be changed upon the next loop
     */
    private boolean changeAnimation = true;

    /**
     * A random number generator used for picking animations
     */
    private Random rng = new Random();

    /**
     * The current state
     */
    private StateType currentState;

    /**
     * The current direction
     */
    private Direction currentDirection;

    /**
     * The listeners which are notified when an animation finished
     */
    private List<BiConsumer<StateType, Direction>> animationFinishedListeners = new ArrayList<>();

    /**
     * Anonymous implementation of {@link AnimationTimer} to aid in testing
     */
    private AnimationTimer animationTimer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            update(now);
        }
    };

    /**
     * Constructor
     * 
     * @param animations An observable map containing {@link AnimationData}
     */
    protected SpritesheetAnimator(ObservableMap<StateType, List<AnimationData>> animations) {
        Bindings.bindContent(this.animations, animations);

        // Load default animation
        defaultAnimation = animations.values().stream()
                .flatMap(Collection::stream)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No animations available to fall back to"));
        currentAnimation = defaultAnimation;

        setAnimation(StateType.IDLE, Direction.NONE);
    }

    private void update(long now) {
        // Check if the spritesheet has changed
        if (changeAnimation) {
            imageProperty.set(currentAnimation.getImage());
            cellProperty.set(viewportFromCell(currentAnimation.getFrames()[0]));
            changeAnimation = false;
            currentFrameID = 0;
        }

        Frame currentFrame = currentAnimation.getFrames()[currentFrameID];

        // If the current timestamp is larger than the timestamp of the last frame plus
        // its duration, change the frame
        if (currentFrame.duration() * 1_000_000 + lastFrame < now) {
            lastFrame = now;
            currentFrameID = (currentFrameID + 1) % currentAnimation.getFrames().length;
            // call the listeners when an animation finished
            if (currentFrameID == 0) {
                animationFinishedListeners.forEach(l -> l.accept(currentState, currentDirection));
            }

            currentFrame = currentAnimation.getFrames()[currentFrameID];
            cellProperty.set(viewportFromCell(currentFrame));
        }

    }

    /**
     * Gets a bindable {@link Image} property that is updated for every change of
     * state or direction
     * 
     * @return An image property that requently changes pictures
     */
    public ReadOnlyObjectProperty<Image> imageProperty() {
        return imageProperty.getReadOnlyProperty();
    }

    /**
     * Gets a bindable {@link Rectangle2D} property that is update every animation
     * frame
     * 
     * @return A rectangle property that frequently changes shape and position
     */
    public ReadOnlyObjectProperty<Rectangle2D> viewportProperty() {
        return cellProperty.getReadOnlyProperty();
    }

    /**
     * Stops the animator and loads an animation based on the provided state and
     * direction. If no animation is found, the default animation is loaded.
     * Usually, this is the first idle animation.
     * 
     * <p>
     * Note: If an animation is not directional (i.e. there is no movement
     * involved), {@link Direction#NONE} should be used instead.
     * </p>
     * 
     * @param state     Which state the animation is assigned to
     * @param direction Which direction the animation is assigned to
     */
    public void setAnimation(StateType state, Direction direction) {
        List<AnimationData> animationCandidates = animations.getOrDefault(state, Collections.emptyList()).stream()
                .filter(animation -> animation.getDirection().equals(direction)).toList();
        animationTimer.stop();
        if (animationCandidates.isEmpty()) {
            currentAnimation = defaultAnimation;
            this.currentState = StateType.IDLE;
            this.currentDirection = Direction.NONE;
            LOGGER.info("Unknown animation %s/%s".formatted(state, direction));
        } else {
            currentAnimation = animationCandidates.get(rng.nextInt(animationCandidates.size()));
            this.currentState = state;
            this.currentDirection = direction;
        }
        changeAnimation = true;
        animationTimer.start();
    }

    /**
     * Gets the current state of the animator
     * 
     * @return The current state
     */
    public StateType getCurrentState() {
        return currentState;
    }

    /**
     * Gets the current direction of the animator
     * 
     * @return The current direction
     */
    public Direction getCurrentDirection() {
        return currentDirection;
    }

    /**
     * Adds a listener which is called when the current animation finishes
     * 
     * @param listener The listener to add
     */
    public void addAnimationFinishedListener(BiConsumer<StateType, Direction> listener) {
        animationFinishedListeners.add(listener);
    }

    /**
     * A helper method to increase readability that creates a {@link Rectangle2D}
     * from a {@link Frame}
     * 
     * @param frame The frame/cell containing dimenions
     * @return A 2D-rectangle
     */
    private Rectangle2D viewportFromCell(Frame frame) {
        return new Rectangle2D(
                frame.cell().positionX(),
                frame.cell().positionY(),
                frame.cell().width(),
                frame.cell().height());
    }
}
