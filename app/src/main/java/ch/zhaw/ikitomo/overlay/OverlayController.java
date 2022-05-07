package ch.zhaw.ikitomo.overlay;

import java.awt.Window.Type;
import java.util.concurrent.CompletableFuture;

import javax.swing.JFrame;

import ch.zhaw.ikitomo.common.Killable;
import ch.zhaw.ikitomo.common.Vector2;
import ch.zhaw.ikitomo.common.tomodachi.TomodachiEnvironment;
import ch.zhaw.ikitomo.overlay.model.OverlayModel;
import ch.zhaw.ikitomo.overlay.view.SpritesheetAnimation;
import javafx.beans.property.ObjectProperty;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;

/**
 * The controller for the overlay that displays the Tomodachi
 */
public class OverlayController implements Killable {
    private static final int WIDTH = 32;
    private static final int HEIGHT = 32;
    private static final String TRANSPARENT_STYLE = "-fx-background-color: rgba(0,0,0,0);";
    /**
     * The overlay model instance
     */
    private OverlayModel model;
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
     * The {@link Image} property from an {@link ImageView}
     */
    private ObjectProperty<Image> imageProperty;
    /**
     * The {@link Rectangle2D} property from an {@link ImageView}
     */
    private ObjectProperty<Rectangle2D> viewportPorperty;
    /**
     * Reference to the animator instance
     */
    private SpritesheetAnimation animator;

    /**
     * Protected controller for {@link OverlayControllerBuilder}
     */
    public OverlayController(TomodachiEnvironment environment, Image image) {
        this.model = new OverlayModel(environment, this);
        this.animator = new SpritesheetAnimation(model.getObservableAnimations());
        imageProperty.bind(animator.getImageProperty());
        viewportPorperty.bind(animator.getCellProperty());

        createPane(image);
        createFrame(pane);

        pane.setOnMouseDragged(dragEvent -> {
            frame.setLocation((int) (frame.getX() + dragEvent.getX() - pane.getWidth() / 2),
                    (int) (frame.getY() + dragEvent.getY() - pane.getHeight() / 2));
        });

    }

    /**
     * Gets the center of the primary monitor
     * @return A vector representing the center point
     */
    public Vector2 getScreenCenter() {
        Rectangle2D screen = Screen.getPrimary().getBounds();
        return new Vector2((int)screen.getMaxX() / 2, (int)screen.getMaxY() / 2);
    }

    @Override
    public CompletableFuture<Void> kill() {
        frame.dispose();
        return CompletableFuture.completedFuture(null);
    }

    /**
     * Creates a pane containing an {@link Image} node. Also stores two
     * ObjectProperties and the pane inside their respective fields
     * 
     * @param image
     */
    private void createPane(Image image) {
        ImageView imageView = new ImageView(image);
        imageView.setStyle(TRANSPARENT_STYLE);
        this.viewportPorperty = imageView.viewportProperty();
        this.imageProperty = imageView.imageProperty();
        this.pane = new Pane();
        pane.setStyle(TRANSPARENT_STYLE);
        this.pane.getChildren().add(imageView);
    }

    /**
     * Spawns a transparent utility window with the specified {@link Pane}
     * 
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
