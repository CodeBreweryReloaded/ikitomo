package ch.zhaw.ikitomo.overlay.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ch.zhaw.ikitomo.common.StateType;
import ch.zhaw.ikitomo.common.Vector2;
import ch.zhaw.ikitomo.common.tomodachi.TomodachiDefinition;
import ch.zhaw.ikitomo.overlay.model.animation.AnimationData;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

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
    private ObservableMap<StateType, List<AnimationData>> animations = FXCollections.observableHashMap();

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
     * 
     * @return An observable map
     */
    public ObservableMap<StateType, List<AnimationData>> getObservableAnimations() {
        return animations;
    }

}
