package ch.zhaw.ikitomo.overlay.model;

import ch.zhaw.ikitomo.common.tomodachi.TomodachiEnvironment;
import ch.zhaw.ikitomo.overlay.OverlayController;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;

/**
 * The model for the {@link ch.zhaw.ikitomo.overlay.OverlayController}
 */
public class OverlayModel {

    /**
     * The current tomodachi environment object
     */
    private TomodachiEnvironment environment;
    /**
     * A reference to the controller that created this model
     */
    private OverlayController controller;
    /**
     * The currently loaded tomodachi
     */
    private ObjectBinding<TomodachiModel> tomodachi;

    /**
     * Constructor
     *
     * @param environment The global settings object
     */
    public OverlayModel(TomodachiEnvironment environment, OverlayController controller) {
        this.environment = environment;
        this.controller = controller;
        tomodachi = Bindings.createObjectBinding(this::loadTomodachiModel,
                environment.getSettings().tomodachiIDProperty());
    }

    /**
     * Gets the binding of the currently selected tomodachi
     *
     * @return The binding of the currently selected tomodachi
     */
    public ObjectBinding<TomodachiModel> tomodachiBinding() {
        return tomodachi;
    }

    /**
     * Gets the currently selected tomodachi
     *
     * @return The currently selected tomodachi
     */
    public TomodachiModel getTomodachi() {
        return tomodachi.get();
    }

    /**
     * Loads a Tomodachi model from the currently loaded Tomodachi
     * 
     * @return The loaded Tomodachi model
     */
    private TomodachiModel loadTomodachiModel() {
        return TomodachiModel.loadFromTomodachiFile(environment, controller.getScreenCenter());
    }



}
