package ch.zhaw.ikitomo.overlay;

import java.awt.Window.Type;

import javax.swing.JFrame;

import ch.zhaw.ikitomo.common.settings.Settings;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * This is a helper class that prepares various UI elements that it passes on to
 * an {@link OverlayController} when it's done.
 */
public class OverlayControllerBuilder {
    /**
     * Window width
     */
    private final int width;
    /**
     * Window height
     */
    private final int height;

    /**
     * The controller instance
     */
    private OverlayController controller;
    /**
     * The frame instance
     */
    private JFrame frame;
    /**
     * The panel instance
     */
    private JFXPanel panel;
    /**
     * The pane instance
     */
    private Pane pane;
    /**
     * The scene instance
     */
    private Scene scene;
    /**
     * The JFX ImageView instance
     */
    private ImageView imageView;

    /**
     * Constructor
     * 
     * @param width  The width to be used in dimensional operations
     * @param height The height to be used in dimensional operations
     */
    public OverlayControllerBuilder(int width, int height) {
        this.width = width;
        this.height = height;
        controller = new OverlayController();
    }

    /**
     * Creates and {@link ImageView} object and stores it internally
     * 
     * @param image The defaultImage to be displayed
     */
    public void createImageView(Image image) {
        imageView = new ImageView(image);
        imageView.setStyle("-fx-background-color: rgba(0,0,0,0);");
    }

    /**
     * Creates a scene and stores it internally. Also appends a previously created
     * {@link ImageView}
     */
    public void createScene() {
        pane = new Pane();
        pane.getChildren().add(imageView);
        scene = new Scene(pane, width, height);
        scene.setFill(Color.TRANSPARENT);
    }

    /**
     * Creates a {@link JFXPanel} and appends a previously created {@link Scene}
     */
    public void createPanel() {
        if (scene == null) {
            throw new IllegalStateException("Cannot create JFXPanel. Scene does not exist");
        }
        panel = new JFXPanel();
        panel.setScene(scene);
    }

    /**
     * Creates a {@link JFrame} and appends a previously created {@link JFXPanel}
     */
    public void createFrame() {
        if (panel == null) {
            throw new IllegalStateException("Cannot create JFrame. JFXPanel does not exist");
        }
        frame = new JFrame();
        frame.getContentPane().add(panel);
        frame.setSize(width, height);
        frame.setResizable(false);
        frame.setUndecorated(true);
        frame.setAlwaysOnTop(true);
        frame.setType(Type.UTILITY);
        frame.setBackground(new java.awt.Color(0.0f, 0.0f, 0.0f, 0.0f));
        frame.setVisible(true);
    }

    /**
     * Finally add all relevant variables to a controller and returns it
     * 
     * @param settings The {@link Settings} object to be used
     * @return The created {@link OverlayController} object
     */
    public OverlayController getController(Settings settings) {
        controller.setSettings(settings);
        controller.setFrame(frame);
        controller.setPane(pane);
        controller.setImageProperty(imageView.imageProperty());
        return controller;
    }

}
