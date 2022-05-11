package ch.zhaw.ikitomo.overlay.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ch.zhaw.ikitomo.common.StateType;
import ch.zhaw.ikitomo.common.Vector2;
import ch.zhaw.ikitomo.common.settings.Settings;
import ch.zhaw.ikitomo.common.tomodachi.TomodachiDefinition;
import ch.zhaw.ikitomo.common.tomodachi.TomodachiEnvironment;
import ch.zhaw.ikitomo.common.tomodachi.TomodachiSettings;
import ch.zhaw.ikitomo.exception.MissingAnimationException;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

/**
 * Tests the {@link OverlayModel}
 */
public class OverlayModelTest {
    
    /**
     * Mocked environment
     */
    @Mock
    TomodachiEnvironment environment;

    /**
     * Settings
     */
    Settings settings;

    /**
     * The overlay instane
     */
    OverlayModel model;

    TomodachiDefinition definition;

    /**
     * Setup function that is run before each test
     */
    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        
        settings = Settings.createDefaultSettings();
        definition = new TomodachiDefinition(null, null, null, null, null, Collections.EMPTY_LIST);
        // Setup mocks
        when(environment.getSettings()).thenReturn(settings);
        when(environment.getCurrentTomodachiDefinition()).thenReturn(definition);
        when(environment.getDefaultTomodachiDefinition()).thenReturn(definition);

        model = new OverlayModel(environment);

    }

    /**
     * Checks if failing to load a Tomodachi correctly falls back to the default one
     */
    @Test
    void testLoadTomodachiFailure() {
        model.loadTomodachiModel();
        verify(model).loadDefaultTomodachiModel();
    }

}
