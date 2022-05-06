package ch.zhaw.ikitomo.overlay.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import ch.zhaw.ikitomo.common.JSONManager;
import ch.zhaw.ikitomo.common.StateType;
import ch.zhaw.ikitomo.common.Vector2;
import ch.zhaw.ikitomo.common.tomodachi.TomodachiDefinition;
import ch.zhaw.ikitomo.common.tomodachi.TomodachiEnvironment;
import ch.zhaw.ikitomo.overlay.model.animation.AnimationData;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableMap;
import javafx.scene.image.Image;

/**
 * Represents a the model for the
 * {@link ch.zhaw.ikitomo.overlay.OverlayController}.
 * <p>
 * This model also includes loaded resources like animations and fields needed
 * to display a Tomodachi, such as position and velocity.
 * </p>
 */
public class TomodachiModel {
    /**
    * The file extension that will be used to read images
    */
    private static final String IMAGE_FORMAT = ".png";
    /**
     * The file extension that will be used to read metadata
     */
    private static final String METADATA_FORMAT = ".json";
    /**
     * The id of the tomodachi
     */
    private String id;
    /**
     * The name of the tomodachi
     */
    private String name;

    /**
     * The position property
     */
    private ObjectProperty<Vector2> position = new SimpleObjectProperty<>();

    /**
     * The velocity property
     */
    private ObjectProperty<Vector2> velocity = new SimpleObjectProperty<>(Vector2.ZERO);

    /**
     * The available states of the tomodachi
     */
    private List<TomodachiModelState> states = new ArrayList<>();

    /**
     * A hash map containing all available animations for each {@link StateType}
     */
    private ObservableMap<StateType, List<AnimationData>> animations;

    /**
     * Constructor
     * 
     * @param id       The id
     * @param name     The name
     * @param position The position
     * @param velocity The velocity
     * @param states   The states
     */
    public TomodachiModel(TomodachiDefinition definition, Map<StateType, List<AnimationData>> animations) {
        this.id = definition.getID();
        this.name = definition.getName();
        this.animations.putAll(animations);
    }

    /**
     * Gets the id of the tomodachi
     *
     * @return The id of the tomodachi
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the name of the tomodachi
     *
     * @return The name of the tomodachi
     */
    public String getName() {
        return name;
    }

    /**
     * Gets a copy of the list of available states
     *
     * @return A copy of the list of available states
     */
    public List<TomodachiModelState> getStates() {
        return new ArrayList<>(states);
    }

    /**
     * Provides an observable map containing all currently loaded animations
     * @return An observable map
     */
    public ObservableMap<StateType, List<AnimationData>> getObservableAnimations() {
        return animations;
    }

    /**
     * Creates complete {@link TomodachiModel} based on information inside the
     * provided {@link TomodachiEnvironment}. It is also positioned at the given
     * vector
     * 
     * @param environment The current environment
     * @param position    The desired initial position
     * @return A working Tomodachi model
     */
    public static TomodachiModel loadFromTomodachiFile(TomodachiEnvironment environment, Vector2 position) {
        // TODO: Stop animation timer to prevent potential weirdness
        Map<StateType, List<AnimationData>> animations = new EnumMap<>(StateType.class);
        AnimationLoader loader = new AnimationLoader();
        String rootPath = environment.getCurrentTomodachiDefinition().getRootFolder().toString();
        environment.getCurrentTomodachiDefinition().getStates().forEach(state -> {
            // Setup useful variables for later
            StateType type = state.type();
            String prefix = state.animationPrefix();
            // Create AnimationData from each animation
            animations.put(type, state.animations().stream().map(animation -> {
                try {
                    // Load the JSON file
                    AnimationData data = loader.load(rootPath + prefix + animation.animationSuffix() + METADATA_FORMAT);
                    data.setImage(new Image(rootPath + prefix + animation.animationSuffix() + IMAGE_FORMAT));
                    data.setDirection(animation.direction());
                    return data;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            })
                    .collect(Collectors.toList()));
        });
        // Remove potential null entries introduced by an IOException
        animations.forEach((state, data) -> data.removeIf(Objects::isNull));

        return new TomodachiModel(environment.getCurrentTomodachiDefinition(), animations);
    }

    /**
     * A small {@link JSONManager} extension class for <code>loadFromTomodachiFile()</code>
     */
    static class AnimationLoader extends JSONManager<AnimationData> {
        protected AnimationLoader() {
            super(AnimationData.class);
        }
    }
}
