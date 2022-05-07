package ch.zhaw.ikitomo.overlay.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import ch.zhaw.ikitomo.common.JSONManager;
import ch.zhaw.ikitomo.common.StateType;
import ch.zhaw.ikitomo.common.Vector2;
import ch.zhaw.ikitomo.common.tomodachi.TomodachiAnimationDefinition;
import ch.zhaw.ikitomo.common.tomodachi.TomodachiDefinition;
import ch.zhaw.ikitomo.common.tomodachi.TomodachiEnvironment;
import ch.zhaw.ikitomo.common.tomodachi.TomodachiStateDefinition;
import ch.zhaw.ikitomo.overlay.model.animation.AnimationData;
import javafx.scene.image.Image;

public class TomodachiModelLoader {
    /**
     * The file extension that will be used to read metadata
     */
    private static final String METADATA_FORMAT = ".json";
    /**
     * The file extension that will be used to read images
     */
    private static final String IMAGE_FORMAT = ".png";

    private TomodachiDefinition definition;
    private Vector2 initialPosition;
    private String rootPath;

    public TomodachiModelLoader(TomodachiDefinition definition, Vector2 initialPosition) {
        this.definition = definition;
        this.initialPosition = initialPosition;
        this.rootPath = definition.isResource() ? null : definition.getRootFolder().toString();
    }

    /**
     * Creates complete {@link TomodachiModel} based on information inside the
     * provided {@link TomodachiEnvironment}. It is also positioned at the given
     * vector
     * 
     * @param environment The current environment
     * @param position    The desired initial position
     * @return A working Tomodachi model
     */
    public TomodachiModel loadFromTomodachiFile() {
        Map<StateType, List<AnimationData>> animations = new EnumMap<>(StateType.class);

        for (TomodachiStateDefinition state : definition.getStates()) {
            animations.put(state.type(), loadAnimations(state));
        }

        return new TomodachiModel(definition, animations);
    }

    private List<AnimationData> loadAnimations(TomodachiStateDefinition state) {
        String prefix = state.animationPrefix();
        // Create AnimationData from each animation
        return state.animations().stream().<AnimationData>mapMulti((animation, consumer) -> {
            try {
                consumer.accept(loadAnimationData(prefix, animation));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).toList();
    }

    private AnimationData loadAnimationData(String prefix, TomodachiAnimationDefinition animation) throws IOException {
        // Load the JSON file
        AnimationData data;
        String pathToMetadata = prefix + animation.animationSuffix() + METADATA_FORMAT;
        String pathToSpritesheet = prefix + animation.animationSuffix() + IMAGE_FORMAT;
        if (definition.isResource()) {
            data = loadResource(pathToMetadata, pathToSpritesheet);
        } else {
            data = loadFile(rootPath + pathToMetadata, rootPath + pathToSpritesheet);
        }
        data.setDirection(animation.direction());
        return data;
    }

    private AnimationData loadResource(String pathToMetadata, String pathToSpritesheet) throws IOException {
        try (InputStream metadataStream = getClass().getClassLoader().getResourceAsStream(pathToMetadata);
                InputStream spritesheetStream = getClass().getClassLoader().getResourceAsStream(pathToSpritesheet);) {

            if (metadataStream == null || spritesheetStream == null) {
                throw new IOException("Animation %s or %s were not found".formatted(pathToMetadata, pathToSpritesheet));
            }

            var in = new InputStreamReader(metadataStream);
            AnimationData data = new AnimationLoader().load(in);
            data.setImage(new Image(spritesheetStream));
            return data;
        }
    }

    private AnimationData loadFile(String pathToMetadata, String pathToSpritesheet) throws IOException {
        AnimationData data = new AnimationLoader().load(pathToMetadata);
        data.setImage(new Image(pathToSpritesheet));
        return data;
    }

    /**
     * A small {@link JSONManager} extension class for
     * <code>loadFromTomodachiFile()</code>
     */
    static class AnimationLoader extends JSONManager<AnimationData> {
        protected AnimationLoader() {
            super(AnimationData.class);
        }
    }
}
