package ch.zhaw.ikitomo.settings.view;

import ch.zhaw.ikitomo.common.DelayedRunnable;
import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

/**
 * A pane which can show a notification at the bottom of the screen
 */
public class BottomNotificationPane extends Pane {
    /**
     * The duration of the animation
     */
    private static final Duration ANIMATION_DURATION = Duration.seconds(0.2);

    /**
     * The timeline object
     */
    private Timeline timeline = new Timeline();

    /**
     * The label which shows the notification
     */
    private Label label = new Label();

    /**
     * The pane which contains the label and is moved by the animation
     */
    private BorderPane borderPane = new BorderPane(label);

    /**
     * A delayed runnable for hiding the notification
     */
    private DelayedRunnable autoHideDelayedRunnable;

    /**
     * Constructor
     */
    public BottomNotificationPane() {
        this(Duration.seconds(4));
    }

    /**
     * Constructor
     * 
     * @param autoHideDuration The duration after which the notification is hidden
     */
    public BottomNotificationPane(Duration autoHideDuration) {
        getChildren().setAll(borderPane);
        borderPane.prefWidthProperty().bind(widthProperty());
        borderPane.setPrefHeight(40);
        borderPane.setStyle("-fx-background-color: f8d7da;");

        setVisible(false);

        autoHideDelayedRunnable = new DelayedRunnable(this::playHideAnimation, (long) autoHideDuration.toMillis());
    }

    /**
     * Shows the notification. If a notification is already up then the text is
     * replaced and the hide timer is reset
     * 
     * @param text The text of the animation
     */
    public void showText(String text) {
        label.setText(text);
        if (!isVisible() || timeline.getStatus() != Status.STOPPED) {
            playShowAnimation();
        }
        setVisible(true);
        autoHideDelayedRunnable.run();
    }

    /**
     * Plays the animation for showing the notification
     */
    private void playShowAnimation() {
        timeline.stop();
        setTranslateY(borderPane.getHeight());
        timeline.getKeyFrames().setAll(createKeyFrame(Duration.ZERO, 0),
                createKeyFrame(ANIMATION_DURATION, -borderPane.getHeight()));
        timeline.play();
    }

    /**
     * Plays the animation for hiding the notification
     */
    private void playHideAnimation() {
        timeline.stop();
        timeline.getKeyFrames().setAll(createKeyFrame(Duration.ZERO, -borderPane.getHeight()),
                createKeyFrame(ANIMATION_DURATION, 0, false));
        timeline.play();
    }

    /**
     * A helper function to create a key frame
     * 
     * @param duration The time of the key frame
     * @param y        The y position of the pane
     * @return The created key frame
     */
    private KeyFrame createKeyFrame(Duration duration, double y) {
        return new KeyFrame(duration, new KeyValue(borderPane.translateYProperty(), y));
    }

    /**
     * A helper function to create a key frame
     * 
     * @param duration The time of the key frame
     * @param y        The y position of the pane
     * @param visible  The visibility of the pane
     * @return The created key frame
     */
    private KeyFrame createKeyFrame(Duration duration, double y, boolean visible) {
        return new KeyFrame(duration, new KeyValue(borderPane.translateYProperty(), y),
                new KeyValue(visibleProperty(), visible));
    }

}
