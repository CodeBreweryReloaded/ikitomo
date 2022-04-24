package ch.zhaw.ikitomo.overlay.model;

import ch.zhaw.ikitomo.common.settings.Settings;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;

/**
 * the model for the {@link ch.zhaw.ikitomo.overlay.OverlayController}
 */
public class OverlayModel {
    /**
     * the global settings object
     */
    private Settings settings;
    /**
     * the currently loaded tomodachi
     */
    private ObjectBinding<TomodachiModel> tomodachi;

    /**
     * @param settings
     */
    public OverlayModel(Settings settings) {
        this.settings = settings;
        tomodachi = Bindings.createObjectBinding(this::loadTomodachiModel, settings.tomodachiFileProperty());
    }

    /**
     * @return the tomodachi binding of the currently selected tomodachi
     */
    public ObjectBinding<TomodachiModel> tomodachiBinding() {
        return tomodachi;
    }

    /**
     * @returnt the currently selected tomodachi
     */
    public TomodachiModel getTomodachi() {
        return tomodachi.get();
    }

    /**
     * @return the settings
     */
    public Settings getSettings() {
        return settings;
    }

    /**
     * Loads a tomodachi model from currently loaded tomodachi
     * 
     * @return the loaded tomodachi model
     */
    private TomodachiModel loadTomodachiModel() {
        throw new UnsupportedOperationException("not implemented yet");
    }

}
