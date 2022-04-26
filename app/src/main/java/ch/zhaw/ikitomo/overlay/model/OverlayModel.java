package ch.zhaw.ikitomo.overlay.model;

import ch.zhaw.ikitomo.common.settings.Settings;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;

/**
 * The model for the {@link ch.zhaw.ikitomo.overlay.OverlayController}
 */
public class OverlayModel {
    /**
     * The global settings object
     */
    private Settings settings;
    /**
     * The currently loaded tomodachi
     */
    private ObjectBinding<TomodachiModel> tomodachi;

    /**
     * Constructor
     *
     * @param settings The global settings object
     */
    public OverlayModel(Settings settings) {
        this.settings = settings;
        tomodachi = Bindings.createObjectBinding(this::loadTomodachiModel, settings.tomodachiFileProperty());
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
     * Gets the global settings
     *
     * @return The global settings
     */
    public Settings getSettings() {
        return settings;
    }

    /**
     * Loads a Tomodachi model from the currently loaded Tomodachi
     * 
     * @return The loaded Tomodachi model
     */
    private TomodachiModel loadTomodachiModel() {
        throw new UnsupportedOperationException("not implemented yet");
    }

}
