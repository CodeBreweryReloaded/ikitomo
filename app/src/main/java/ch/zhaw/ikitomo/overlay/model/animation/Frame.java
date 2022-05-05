package ch.zhaw.ikitomo.overlay.model.animation;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Frame {
    @JsonProperty("frame")
    public Cell cell;
    @JsonProperty("duration")
    public int duration;
}
