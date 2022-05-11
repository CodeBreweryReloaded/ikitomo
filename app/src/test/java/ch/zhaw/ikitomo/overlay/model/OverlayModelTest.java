package ch.zhaw.ikitomo.overlay.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ch.zhaw.ikitomo.common.settings.Settings;
import ch.zhaw.ikitomo.common.settings.SettingsManager;
import ch.zhaw.ikitomo.common.tomodachi.TomodachiDefinition;
import ch.zhaw.ikitomo.common.tomodachi.TomodachiEnvironment;
import ch.zhaw.ikitomo.common.tomodachi.TomodachiManager;

/**
 * Tests the {@link OverlayModel}
 */
public class OverlayModelTest {

    /**
     * Mocked environment
     */
    @Mock
    private TomodachiEnvironment environment;

    /**
     * The overlay instance
     */
    private OverlayModel model;

    /**
     * The default tomodachi definition
     */
    private TomodachiDefinition defaultDefinition;

    /**
     * Setup function that is run before each test
     */
    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        Settings settings = Settings.createDefaultSettings();
        defaultDefinition = new TomodachiEnvironment(new SettingsManager(), new TomodachiManager())
                .getDefaultTomodachiDefinition();
        // Setup mocks
        when(environment.getSettings()).thenReturn(settings);
        when(environment.getCurrentTomodachiDefinition()).thenReturn(defaultDefinition);
        when(environment.getDefaultTomodachiDefinition()).thenReturn(defaultDefinition);

        model = new OverlayModel(environment);

    }

    /**
     * Checks if failing to load a Tomodachi correctly falls back to the default one
     */
    @Test
    void testLoadTomodachiFailure() {
        var testDefinition = new TomodachiDefinition(null, "test", "Test", null, null, Collections.emptyList());
        when(environment.getCurrentTomodachiDefinition()).thenReturn(testDefinition);
        TomodachiModel tomodachiModel = model.loadTomodachiModel();
        assertEquals(defaultDefinition.getID(), tomodachiModel.getId());
    }

}
