package ch.zhaw.ikitomo.overlay.model.animation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A record containing the dimensions of a single cell in a spritesheet
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record Cell(
                /**
                 * Specifies the horizontal offset of the cell from the top left corner. In
                 * other words: "This many pixels from (0,0) to the right"
                 */
                @JsonProperty("x") int positionX,
                /**
                 * Specifies the vertical offset of the cell from the top left corner. In other
                 * words: "This many pixels form (0,0) to the bottom"
                 */
                @JsonProperty("y") int positionY,
                /**
                 * Width of the cell
                 */
                @JsonProperty("w") int width,
                /**
                 * Height of the cell
                 */
                @JsonProperty("h") int height) {
}
