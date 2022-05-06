package ch.zhaw.ikitomo.common.tomodachi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ch.zhaw.ikitomo.common.Direction;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TomodachiAnimationDefinition(
        Direction direction,
        String animationSuffix) {

}
