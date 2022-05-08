package ch.zhaw.ikitomo.overlay;

import java.awt.Window.Type;
import java.util.concurrent.CompletableFuture;

import javax.swing.JFrame;

import ch.zhaw.ikitomo.common.Killable;
import ch.zhaw.ikitomo.common.Vector2;
import ch.zhaw.ikitomo.common.tomodachi.TomodachiEnvironment;
import ch.zhaw.ikitomo.exception.MissingAnimationException;
import ch.zhaw.ikitomo.overlay.model.OverlayModel;
import ch.zhaw.ikitomo.overlay.view.AnimatedImageView;
import ch.zhaw.ikitomo.overlay.view.SpritesheetAnimation;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;

/**
 * The controller for the overlay that displays the Tomodachi
 */
public class OverlayController implements Killable {
    /**
     * The window width
     */
    private static final int WIDTH = 32;

    /**
     * The window height
     */
    private static final int HEIGHT = 32;

    /**
     * A JFX style to make a background transparent
     */
    public static final String TRANSPARENT_STYLE = "-fx-background-color: rgba(0,0,0,0);";

    /**
     * The overlay model instance
     */
    private OverlayModel model;

    /**
     * Field containing the JFX Frame
     */
    private JFrame frame;

    /**
     * The JFX root pane
     */
    private Pane pane;

    /**
     * The animated Tomodachi sprite
     */
    private AnimatedImageView animatedImage;

    private SpritesheetAnimation animator;

    /**
     * Protected controller for {@link OverlayControllerBuilder}
     * 
     * @throws MissingAnimationException
     */
    public OverlayController(TomodachiEnvironment environment) {
        createPane();
        createFrame(pane);

        model = new OverlayModel(environment, this);
        model.getTomodachi().setPosition(getScreenCenter());
        try {
            animatedImage = new AnimatedImageView(model.getObservableAnimations());
        } catch (MissingAnimationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        animator = animatedImage.getAnimator();
        pane.getChildren().add(animatedImage);
        animator.start();

        pane.setOnMouseDragged(this::onDragEvent);

        // setup position listener
        model.tomodachiPositionBinding().addListener((observable, oldValue, newValue) -> setLocation(newValue));

        // setup animatino listeners
        model.tomodachiStateBinding().addListener((observable, oldValue, newValue) -> updateAnimation());
        model.tomodachiDirectionBinding().addListener((observable, oldValue, newValue) -> updateAnimation());

        // if the animation finished, tell the current behavior strategy
        animatedImage.getAnimator().addAnimationFinishedListener(
                (state, direction) -> model.getBehaviorStrategy().animationFinished(state));
    }

    private void updateAnimation() {
        animator.setAnimation(model.getTomodachiState(), model.getTomodachiDirection());
    }

    private void onDragEvent(MouseEvent dragEvent) {
        var pos = new Vector2((float) (frame.getX() + dragEvent.getX()), (float) (frame.getY() + dragEvent.getY()));
        model.getTomodachi().setPosition(pos);
    }

    private void setLocation(Vector2 position) {
        frame.setLocation((int) (position.x() - pane.getWidth() / 2), (int) (position.y() - pane.getHeight() / 2));
    }

    /**
     * Gets the center of the primary monitor
     * 
     * @return A vector representing the center point
     */
    public Vector2 getScreenCenter() {
        Rectangle2D screen = Screen.getPrimary().getBounds();
        return new Vector2((int) screen.getMaxX() / 2, (int) screen.getMaxY() / 2);
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
    private void createPane() {
        this.pane = new Pane();
        pane.setStyle(TRANSPARENT_STYLE);
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
