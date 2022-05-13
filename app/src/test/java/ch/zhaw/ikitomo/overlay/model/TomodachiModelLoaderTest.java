package ch.zhaw.ikitomo.overlay.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ch.zhaw.ikitomo.common.Direction;
import ch.zhaw.ikitomo.common.StateType;
import ch.zhaw.ikitomo.common.settings.Settings;
import ch.zhaw.ikitomo.common.settings.SettingsManager;
import ch.zhaw.ikitomo.common.tomodachi.TomodachiAnimationDefinition;
import ch.zhaw.ikitomo.common.tomodachi.TomodachiDefinition;
import ch.zhaw.ikitomo.common.tomodachi.TomodachiEnvironment;
import ch.zhaw.ikitomo.common.tomodachi.TomodachiManager;
import ch.zhaw.ikitomo.common.tomodachi.TomodachiSettings;
import ch.zhaw.ikitomo.common.tomodachi.TomodachiStateDefinition;
import ch.zhaw.ikitomo.exception.MissingAnimationException;
import ch.zhaw.ikitomo.overlay.model.animation.AnimationData;

/**
 * Tests the {@link TomodachiModelLoader}
 */
class TomodachiModelLoaderTest {
    /**
     * The default definition
     */
    private TomodachiDefinition defaultDefinition;

    /**
     * A settings object
     */
    private Settings settings = Settings.createDefaultSettings();

    /**
     * Setup method for the test
     */
    @BeforeEach
    void setup() {
        this.defaultDefinition = new TomodachiEnvironment(new SettingsManager(), new TomodachiManager())
                .getDefaultTomodachiDefinition();
    }

    /**
     * Tests if the {@link TomodachiModelLoader#loadFromTomodachiFile()} throws a
     * {@link MissingAnimationException} when no animations are present
     */
    @Test
    void testLoadFromTomodachiFileThrowExceptionOnNoAnimations() {
        var definition = new TomodachiDefinition(null, "ch.ikitomo.test", "Tets", new TomodachiSettings(), "",
                Collections.emptyList());
        var loader = new TomodachiModelLoader(definition, settings);
        assertThrows(MissingAnimationException.class, () -> loader.loadFromTomodachiFile());
    }

    /**
     * Tests if the {@link TomodachiModelLoader#loadFromTomodachiFile()} loads the
     * default definition from the classpath correctly
     */
    @Test
    void testLoadingFromClasspath() throws MissingAnimationException {
        var loader = new TomodachiModelLoader(defaultDefinition, settings);
        var model = loader.loadFromTomodachiFile();
        List<TomodachiStateDefinition> stateDefinitions = defaultDefinition.getStates();
        Map<StateType, List<AnimationData>> animations = model.getObservableAnimations();
        for (var stateDefinition : stateDefinitions) {
            List<AnimationData> animationList = animations.get(stateDefinition.type());
            assertNotNull(animationList, "Animation list for state " + stateDefinition.type() + " wasn't loaded");
            assertEquals(stateDefinition.animations().size(), animationList.size());
        }
    }

    /**
     * Tests if the {@link TomodachiModelLoader} sets the id and name correctly and
     * fetches the settings object from the settings or taking the default the
     * settings object doesn't exist yet
     */
    @Test
    void testIfFieldsIsCorrectlyReferenced() throws MissingAnimationException {
        var loader = new TomodachiModelLoader(defaultDefinition, settings);
        var model = loader.loadFromTomodachiFile();
        // the settings have to be exactly the same reference
        assertSame(settings.getTomodachiSettings(model.getId()), model.getSettings());

        // the same settings have to be used when loaded again
        var model2 = loader.loadFromTomodachiFile();
        // the settings have to be exactly the same reference
        assertSame(settings.getTomodachiSettings(model2.getId()), model2.getSettings());
        assertSame(model.getSettings(), model2.getSettings());
        assertEquals(defaultDefinition.getID(), model.getId());
        assertEquals(defaultDefinition.getName(), model.getName());
    }

    /**
     * Tests if the {@link TomodachiModelLoader} continues loading even when an
     * animation fails loading
     */
    @Test
    void testContinueLoadingIfOneAnimationFailsToLoad() throws MissingAnimationException {
        // create animation definition list based on the animations from the default
        List<TomodachiStateDefinition> animationDefinitionList = new ArrayList<>(defaultDefinition.getStates());
        animationDefinitionList.add(new TomodachiStateDefinition(StateType.WAKE, "test",
                Arrays.asList(new TomodachiAnimationDefinition(Direction.NONE, "invalid/path"))));
        var definition = new TomodachiDefinition(null, "ch.ikitomo.test2", "Test2", new TomodachiSettings(), "",
                animationDefinitionList);
        var loader = new TomodachiModelLoader(definition, settings);
        var model = loader.loadFromTomodachiFile();
        List<TomodachiStateDefinition> stateDefinitions = defaultDefinition.getStates();
        Map<StateType, List<AnimationData>> animations = model.getObservableAnimations();
        for (var stateDefinition : stateDefinitions) {
            List<AnimationData> animationList = animations.get(stateDefinition.type());
            assertNotNull(animationList, "Animation list for state " + stateDefinition.type() + " wasn't loaded");
            assertEquals(stateDefinition.animations().size(), animationList.size());
        }

    }
}
