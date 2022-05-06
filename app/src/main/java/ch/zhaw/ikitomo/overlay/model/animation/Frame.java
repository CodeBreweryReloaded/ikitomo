package ch.zhaw.ikitomo.overlay.model.animation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Contains cell and duration of single animation frame
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record Frame(
        /**
         * The spritesheet cell
         */
        @JsonProperty("frame") Cell cell,
        /**
         * The duration of this frame in miliseconds
         */
        @JsonProperty("duration") int duration) {
}
