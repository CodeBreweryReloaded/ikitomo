package ch.zhaw.ikitomo.overlay;

import java.util.concurrent.CompletableFuture;

import javax.swing.JFrame;

import ch.zhaw.ikitomo.common.Killable;
import ch.zhaw.ikitomo.common.settings.Settings;
import javafx.beans.property.ObjectProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * The controller for the overlay that displays the Tomodachi
 */
public class OverlayController implements Killable {

    /**
     * The global settings object
     */
    private Settings settings;
    /**
     * Field containing the JFX Frame
     */
    private JFrame frame;
    /**
     * The JFX root pane
     */
    private Pane pane;
    /**
     * The image property from an {@link ImageView}
     */
    private ObjectProperty<Image> imageProperty;

    /**
     * Protected controller for {@link OverlayControllerBuilder}
     */
    protected OverlayController() {
    }

    /**
     * A setup method that initializes bindings and EventListeners
     */
    public void setup() {
        pane.setOnMouseDragged(dragEvent -> {
            frame.setLocation((int) (frame.getX() + dragEvent.getX() - pane.getWidth() / 2),
                    (int) (frame.getY() + dragEvent.getY() - pane.getHeight() / 2));
        });
    }

    @Override
    public CompletableFuture<Void> kill() {
        frame.dispose();
        return CompletableFuture.completedFuture(null);
    }

    /**
     * Sets settings
     * 
     * @param settings The desired settings to set
     */
    protected void setSettings(Settings settings) {
        this.settings = settings;
    }

    /**
     * Sets the frame
     * 
     * @param frame The JFX frame
     */
    protected void setFrame(JFrame frame) {
        this.frame = frame;
    }

    /**
     * Sets the pane contained in the JFX frame
     * 
     * @param pane The pane
     */
    protected void setPane(Pane pane) {
        this.pane = pane;
    }

    /**
     * Sets the {@link ObjectProperty} from the {@link ImageView}
     * 
     * @param imageProperty The property
     */
    protected void setImageProperty(ObjectProperty<Image> imageProperty) {
        this.imageProperty = imageProperty;
    }
}
