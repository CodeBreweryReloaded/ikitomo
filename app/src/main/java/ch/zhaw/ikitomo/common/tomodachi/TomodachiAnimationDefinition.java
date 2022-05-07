package ch.zhaw.ikitomo.common.tomodachi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import ch.zhaw.ikitomo.common.Direction;

/**
 * Defines the variables of an animation
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record TomodachiAnimationDefinition(
        @JsonProperty("direction") Direction direction,
        @JsonProperty("animationSuffix") String animationSuffix) {

}
