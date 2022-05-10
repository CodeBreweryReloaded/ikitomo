package ch.zhaw.ikitomo.overlay.model.animation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A record containing the dimensions of a single cell in a spritesheet
 * 
 * @param positionX Specifies the horizontal offset of the cell from the top
 *                  left corner. In other words: "This many pixels from (0,0) to
 *                  the right"
 * @param positionY Specifies the vertical offset of the cell from the top left
 *                  corner. In other words: "This many pixels form (0,0) to the
 *                  bottom"
 * @param width     Width of the cell
 * @param height    Height of the cell
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record Cell(
        @JsonProperty("x") int positionX,
        @JsonProperty("y") int positionY,
        @JsonProperty("w") int width,
        @JsonProperty("h") int height) {
}
