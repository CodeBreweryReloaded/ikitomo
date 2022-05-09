package ch.zhaw.ikitomo.common.tomodachi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import ch.zhaw.ikitomo.common.Direction;

/**
 * Defines the variables of an animation
 * @param direction The direction of the animation
 * @param animationSuffix The suffix of the animation filename
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record TomodachiAnimationDefinition(
        @JsonProperty("direction") Direction direction,
        @JsonProperty("animationSuffix") String animationSuffix) {

}
