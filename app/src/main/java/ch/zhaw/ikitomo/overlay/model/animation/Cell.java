package ch.zhaw.ikitomo.overlay.model.animation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Cell(
        @JsonProperty("x") int positionX,
        @JsonProperty("y") int positionY,
        @JsonProperty("w") int width,
        @JsonProperty("h") int height) {
}
