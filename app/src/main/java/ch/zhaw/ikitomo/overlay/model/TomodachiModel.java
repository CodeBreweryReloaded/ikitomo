package ch.zhaw.ikitomo.overlay.model;

import java.util.ArrayList;
import java.util.List;

import ch.zhaw.ikitomo.common.Vector2;
import ch.zhaw.ikitomo.common.tomodachi.TomodachiDefinition;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

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
     * Constructor
     * 
     * @param id       The id
     * @param name     The name
     * @param position The position
     * @param velocity The velocity
     * @param states   The states
     */
    public TomodachiModel(String id, String name, Vector2 position, Vector2 velocity,
            List<TomodachiModelState> states) {
        this.id = id;
        this.name = name;
        this.position.set(position);
        this.velocity.set(velocity);
        this.states.addAll(states);
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
     * Loads a {@link TomodachiModel} from a given {@link TomodachiDefinition}.
     * <p>
     * The model is set at the given initial position with the velocity of zero.
     * </p>
     * 
     * @param tomodachiFile The tomodachi file
     * @param position      The initial position
     * @return The created model
     */
    public static TomodachiModel loadFromTomodachiFile(TomodachiDefinition tomodachiFile, Vector2 position) {
        TomodachiModel model = new TomodachiModel(tomodachiFile.getConfig().getId(),
                tomodachiFile.getConfig().getName(), position,
                Vector2.ZERO, null);
        throw new UnsupportedOperationException("not implemented yet loading the animations");
    }

}
