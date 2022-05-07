package ch.zhaw.ikitomo.common.tomodachi;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

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
            @JsonProperty("type") StateType type,
            @JsonProperty("animationPrefix") String animationPrefix,
            @JsonProperty("animations") List<TomodachiAnimationDefinition> animations) {
        this.type = type;
        this.animationPrefix = animationPrefix;
        this.animations = new ArrayList<>(animations);
    }
}
