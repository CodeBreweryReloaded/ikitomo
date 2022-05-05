package ch.zhaw.ikitomo.overlay;

import java.awt.Window.Type;
import java.util.concurrent.CompletableFuture;

import javax.swing.JFrame;

import ch.zhaw.ikitomo.common.Killable;
import ch.zhaw.ikitomo.common.tomodachi.TomodachiEnvironment;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
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
    private JFrame frame;

    /**
     * Private constructor
     * 
     * @param environment The global environment object
     */
    private OverlayController(TomodachiEnvironment environment, JFrame frame) {
        this.environment = environment;
    }
    
    @Override
    public CompletableFuture<Void> kill() {
        frame.dispose();
        return CompletableFuture.completedFuture(null);
    }

    /**
     * Creates a new overlay UI and returns the controller
     * 
     * @param environment The global environment object
     * @return The newly created {@link OverlayController}
     */
    public static OverlayController newOverlayUI(TomodachiEnvironment environment, Stage primaryStage) {
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
        
        return new OverlayController(environment, frame);
    }
}
