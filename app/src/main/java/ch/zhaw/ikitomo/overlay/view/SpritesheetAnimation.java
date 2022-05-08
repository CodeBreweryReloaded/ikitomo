package ch.zhaw.ikitomo.overlay.view;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.logging.Logger;

import ch.zhaw.ikitomo.common.Direction;
import ch.zhaw.ikitomo.common.StateType;
import ch.zhaw.ikitomo.exception.MissingAnimationException;
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
     * Logger
     */
    private static final Logger LOGGER = Logger.getLogger(SpritesheetAnimation.class.getName());

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
     * Constructor
     * 
     * @param animations An observable map containing {@link AnimationData}
     * @throws MissingAnimationException When the default animation can't be loaded
     *                                   (first {@link StateType#IDLE} animation)
     */
    protected SpritesheetAnimation(ObservableMap<StateType, List<AnimationData>> animations)
            throws MissingAnimationException {
        Bindings.bindContent(this.animations, animations);
        defaultAnimation = animations.get(StateType.IDLE).get(0);
        currentAnimation = defaultAnimation;
        currentState = StateType.IDLE;
        currentDirection = Direction.NONE;

        if (defaultAnimation == null) {
            throw new MissingAnimationException("First IDLE animation is missing or couldn't be loaded");
        }
    }

    @Override
    public void handle(long now) {
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
     * @return
     */
    public ObjectProperty<Image> getImageProperty() {
        return imageProperty;
    }

    /**
     * Gets a bindable {@link Rectangle2D} property that is update every animation
     * frame
     * 
     * @return
     */
    public ObjectProperty<Rectangle2D> getCellProperty() {
        return cellProperty;
    }

    /**
     * Stops the animator and loads an animation based on the provided state and
     * direction. If no animation is found, the default animation is loaded. By
     * default, this is the first idle animation.
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
        List<AnimationData> animationCandidates = animations.get(state).stream()
                .filter(animation -> animation.getDirection().equals(direction)).toList();
        stop();
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
        start();
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
     * @return the current direction
     */
    public Direction getCurrentDirection() {
        return currentDirection;
    }

    /**
     * Adds a listener which is called when the current animation finishes
     * 
     * @param listener the listener to add
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
