package ch.zhaw.ikitomo.overlay.view;

import java.nio.file.Path;

import ch.zhaw.ikitomo.common.JSONManager;
import ch.zhaw.ikitomo.overlay.model.animation.AnimationData;
import javafx.animation.AnimationTimer;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;

public class SpritesheetAnimation extends AnimationTimer {
    private ObjectProperty<Image> imageProperty;

    public SpritesheetAnimation(ObjectProperty<Image> imageProperty, ObjectProperty<Rectangle2D> viewportProperty,
            Path spritesheet, Path metadata) {
        
    }

    @Override
    public void handle(long now) {
        // TODO Auto-generated method stub

    }

    class AnimationLoader extends JSONManager<AnimationData> {

        protected AnimationLoader() {
            super(AnimationData.class);
        }

    }
}
