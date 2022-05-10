package ch.zhaw.ikitomo.overlay.model.animation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Contains cell and duration of single animation frame
 * 
 * @param cell     The spritesheet cell
 * @param duration The duration of this frame in miliseconds
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record Frame(
                @JsonProperty("frame") Cell cell,
                @JsonProperty("duration") int duration) {
}
