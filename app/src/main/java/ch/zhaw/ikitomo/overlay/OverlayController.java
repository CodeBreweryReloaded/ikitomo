package ch.zhaw.ikitomo.overlay;

import java.awt.Window.Type;
import java.util.concurrent.CompletableFuture;

import javax.swing.JFrame;

import ch.zhaw.ikitomo.common.Killable;
import ch.zhaw.ikitomo.common.settings.Settings;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.embed.swing.JFXPanel;
import javafx.event.Event;

/**
 * The controller for the overlay that displays the Tomodachi
 */
public class OverlayController implements Killable {

    private static final int WIDTH = 32;
    private static final int HEIGHT = 32;
    /**
     * The global settings object
     */
    private Settings settings;
    private JFrame frame;
    private ImageView imageView;
    private Pane pane;



    /**
     * Private constructor
     * 
     * @param settings The global settings object
     */
    private OverlayController(Settings settings, JFrame frame, Pane pane, ImageView imageView) {
        this.settings = settings;
        this.frame = frame;
        this.pane = pane;
        this.imageView = imageView;

        pane.setOnMouseDragged(dragEvent -> {
            frame.setLocation((int)(frame.getX() + dragEvent.getX() - pane.getWidth() / 2), (int)(frame.getY() + dragEvent.getY() - pane.getHeight() / 2));
        });
    }
    
    @Override
    public CompletableFuture<Void> kill() {
        frame.dispose();
        return CompletableFuture.completedFuture(null);
    }

    /**
     * Creates a new overlay UI and returns the controller
     * 
     * @param settings The global settings object
     * @return The newly created {@link OverlayController}
     */
    public static OverlayController newOverlayUI(Settings settings) {
        Pane pane = new Pane();
        Image image = new Image("file:Assets/neko-classic-dev/sprites/awake.png");
        ImageView imageView = new ImageView(image);
        imageView.setStyle("-fx-background-color: rgba(0,0,0,0);");
        pane.getChildren().add(imageView);
        
        Scene scene = new Scene(pane, WIDTH, HEIGHT);
        scene.setFill(Color.TRANSPARENT);

        JFrame frame = new JFrame();
        JFXPanel container = new JFXPanel();
        container.setScene(scene);
        frame.getContentPane().add(container);
        frame.setSize(WIDTH, HEIGHT);
        frame.setResizable(false);
        frame.setUndecorated(true);
        frame.setAlwaysOnTop(true);        
        frame.setType(Type.UTILITY);
        frame.setBackground(new java.awt.Color(0.0f, 0.0f, 0.0f, 0.0f));
        frame.setVisible(true);
        
        return new OverlayController(settings, frame, pane, imageView);
    }
}
