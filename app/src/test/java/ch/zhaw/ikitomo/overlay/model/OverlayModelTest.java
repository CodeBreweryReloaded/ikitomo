package ch.zhaw.ikitomo.overlay.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ch.zhaw.ikitomo.common.Vector2;
import ch.zhaw.ikitomo.common.settings.Settings;
import ch.zhaw.ikitomo.common.tomodachi.TomodachiEnvironment;
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
     * Mocked settings
     */
    @Mock
    Settings settings;

    /**
     * The overlay instane
     */
    OverlayModel model;

    /**
     * Setup function that is run before each test
     */
    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        // Setup mocks
        when(Screen.getPrimary().getBounds()).thenReturn(new Rectangle2D(0, 0, 50, 50));
        when(environment.getSettings()).thenReturn(settings);
        when(settings.getTomodachiID()).thenReturn("ch.zhaw.ikitomo.defaultNeko");
        when(environment.getCurrentTomodachiDefinition()).thenThrow(new MissingAnimationException(""));

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

    /**
     * Checks if the screen center calculation is correct
     */
    @Test
    void testScreenCenter() {
        assertEquals(new Vector2(25.0f, 25.0f), model.getScreenCenter());
    }
}
