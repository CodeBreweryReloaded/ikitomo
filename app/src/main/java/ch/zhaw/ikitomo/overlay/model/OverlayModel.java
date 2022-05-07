package ch.zhaw.ikitomo.overlay.model;

import java.util.List;

import ch.zhaw.ikitomo.common.StateType;
import ch.zhaw.ikitomo.common.Vector2;
import ch.zhaw.ikitomo.common.tomodachi.TomodachiEnvironment;
import ch.zhaw.ikitomo.overlay.OverlayController;
import ch.zhaw.ikitomo.overlay.model.animation.AnimationData;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.collections.ObservableMap;

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

    public ObservableMap<StateType, List<AnimationData>> getObservableAnimations() {
        return tomodachi.get().getObservableAnimations();
    }

    /**
     * Loads a Tomodachi model from the currently loaded Tomodachi
     * 
     * @return The loaded Tomodachi model
     */
    private TomodachiModel loadTomodachiModel() {
        return new TomodachiModelLoader(environment.getCurrentTomodachiDefinition(), controller.getScreenCenter()).loadFromTomodachiFile();
    }

}
