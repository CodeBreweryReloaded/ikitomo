package ch.zhaw.ikitomo.common.tomodachi;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ch.zhaw.ikitomo.common.StateType;

/**
 * Represents the definition of a state of a tomodachi's state machine
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record TomodachiStateDefinition(
        StateType type,
        String animationPrefix,
        List<TomodachiAnimationDefinition> animations) {

    public TomodachiStateDefinition(
            StateType type,
            String animationPrefix,
            List<TomodachiAnimationDefinition> animations) {
        this.type = type;
        this.animationPrefix = animationPrefix;
        this.animations = new ArrayList<>(animations);
    }
}
