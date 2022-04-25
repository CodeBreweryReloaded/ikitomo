package ch.zhaw.ikitomo.overlay.model;

import java.util.ArrayList;
import java.util.List;

import ch.zhaw.ikitomo.common.Vector2;
import ch.zhaw.ikitomo.common.tomodachi.TomodachiFile;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Represents a Tomodachi model for {@link ch.zhaw.ikitomo.overlay.OverlayController}.
 * This model also includes loaded resources like animations and fields needed
 * to display a Tomodachi, such as position and velocity.
 */
public class TomodachiModel {
    /**
     * The id
     */
    private String id;
    /**
     * The name
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
     * The states
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
     * @return The id
     */
    public String getId() {
        return id;
    }

    /**
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @return A copy of the states list
     */
    public List<TomodachiModelState> getStates() {
        return new ArrayList<>(states);
    }

    /**
     * Loads a {@link TomodachiModel} from a given {@link TomodachiFile}.
     * The model is set at the given initial position with the velocity of zero
     * 
     * @param tomodachiFile The tomodachi file
     * @param position      The initial position
     * @return The created model
     */
    public static TomodachiModel loadFromTomodachiFile(TomodachiFile tomodachiFile, Vector2 position) {
        TomodachiModel model = new TomodachiModel(tomodachiFile.getConfig().getId(),
                tomodachiFile.getConfig().getName(), position,
                Vector2.ZERO, null);
        throw new UnsupportedOperationException("not implemented yet loading the animations");
    }

}
