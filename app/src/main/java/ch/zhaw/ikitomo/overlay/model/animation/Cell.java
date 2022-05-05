package ch.zhaw.ikitomo.overlay.model.animation;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Cell {
    @JsonProperty("x")
    public int positionX;
    @JsonProperty("y")
    public int positionY;
    @JsonProperty("w")
    public int width;
    @JsonProperty("h")
    public int height;
}
