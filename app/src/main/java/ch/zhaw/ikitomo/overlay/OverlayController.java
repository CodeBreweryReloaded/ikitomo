package ch.zhaw.ikitomo.overlay;

import java.util.concurrent.CompletableFuture;
import java.awt.Window.Type;

import javax.swing.JFrame;

import ch.zhaw.ikitomo.common.Killable;
import ch.zhaw.ikitomo.common.tomodachi.TomodachiEnvironment;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.beans.property.ObjectProperty;
import javafx.embed.swing.JFXPanel;

/**
 * The controller for the overlay that displays the Tomodachi
 */
public class OverlayController implements Killable {
    private static final int WIDTH = 32;
    private static final int HEIGHT = 32;
    /**
     * The global environment object
     */
    private TomodachiEnvironment environment;
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
    public OverlayController() {

    }

    /**
     * A setup method that initializes bindings and EventListeners
     */
    public void setup(Image image) {
        createPane(image);
        createFrame(pane);

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
     * Creates a scene and stores it internally. Also appends a previously created
     * {@link ImageView}
     */
    private void createPane(Image image) {
        ImageView imageView = new ImageView(image);
        imageView.setStyle("-fx-background-color: rgba(0,0,0,0);");
        this.imageProperty = imageView.imageProperty();
        this.pane = new Pane();
        this.pane.getChildren().add(imageView);
    }
    
    /**
     * Spawns a transparent utility window with the specified {@link Pane}
     * @param pane The pane to show
     */
    private void createFrame(Pane pane) {
        // Create Scene from pane
        Scene scene = new Scene(pane, WIDTH, HEIGHT);
        scene.setFill(Color.TRANSPARENT);

        // Create JFXPanel
        JFXPanel panel = new JFXPanel();
        panel.setScene(scene);

        // Create JFrame
        frame = new JFrame();
        frame.getContentPane().add(panel);
        frame.setSize(WIDTH, WIDTH);
        frame.setResizable(false);
        frame.setUndecorated(true);
        frame.setAlwaysOnTop(true);
        frame.setType(Type.UTILITY);
        frame.setBackground(new java.awt.Color(0.0f, 0.0f, 0.0f, 0.0f));
        frame.setVisible(true);
    }
}
