package ch.zhaw.ikitomo.overlay.model;

import java.util.ArrayList;
import java.util.List;

import ch.zhaw.ikitomo.common.Vector2;
import ch.zhaw.ikitomo.common.tomodachi.TomodachiFile;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * the a tomodachi for the {@link ch.zhaw.ikitomo.overlay.OverlayController}.
 * This model also includes loaded resources like animations and fields needed
 * to display the tomodachi (like the position or velocity).
 */
public class TomodachiModel {
    /**
     * the id
     */
    private String id;
    /**
     * the name
     */
    private String name;

    /**
     * the position property
     */
    private ObjectProperty<Vector2> position = new SimpleObjectProperty<>();

    /**
     * the velocity property
     */
    private ObjectProperty<Vector2> velocity = new SimpleObjectProperty<>(Vector2.ZERO);

    /**
     * the states
     */
    private List<TomodachiModelState> states = new ArrayList<>();

    /**
     * Constructor
     * 
     * @param id       the id
     * @param name     the name
     * @param position the position
     * @param velocity the velocity
     * @param states   the states
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
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return a copy of the states list
     */
    public List<TomodachiModelState> getStates() {
        return new ArrayList<>(states);
    }

    /**
     * Loads a {@link TomodachiModel} from a given {@link TomodachiFile}.
     * The model is set at the given initial position with the velocity of zero
     * 
     * @param tomodachiFile the tomodachi file
     * @param position      the initial position
     * @return the created model
     */
    public static TomodachiModel loadFromTomodachiFile(TomodachiFile tomodachiFile, Vector2 position) {
        TomodachiModel model = new TomodachiModel(tomodachiFile.getConfig().getId(),
                tomodachiFile.getConfig().getName(), position,
                Vector2.ZERO, null);
        throw new UnsupportedOperationException("not implemented yet loading the animations");
    }

}
