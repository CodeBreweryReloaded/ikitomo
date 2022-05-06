package ch.zhaw.ikitomo.overlay.view;

import java.io.IOException;
import java.nio.file.Path;

import ch.zhaw.ikitomo.common.JSONManager;
import ch.zhaw.ikitomo.overlay.model.animation.AnimationData;
import javafx.animation.AnimationTimer;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;

public class SpritesheetAnimation extends AnimationTimer {
    private ObjectProperty<Image> imageProperty;

    public SpritesheetAnimation(ObjectProperty<Image> imageProperty, ObjectProperty<Rectangle2D> viewportProperty) {
        AnimationLoader loader = new AnimationLoader();
        try {
            AnimationData data = loader.load("Assets/neko-classic-dev/sprites/awake.json");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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
