package ch.zhaw.ikitomo.settings.view;

import ch.zhaw.ikitomo.common.DelayedRunnable;
import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * A pane which can show a notification at the bottom of the screen
 */
public class BottomNotificationPane extends Pane {
    /**
     * The duration of the translate animation
     */
    private static final Duration TRANSLATE_ANIMATION_DURATION = Duration.seconds(0.2);
    /**
     * The duration of the color animation
     */
    private static final Duration COLOR_ANIMATION_DURATION = Duration.seconds(0.35);

    /**
     * The color of the error notification
     */
    private static final Color ERROR_BACKGROUND_COLOR = new Color(0.97, 0.84, 0.85, 1);
    /**
     * The color of the info notification
     */
    private static final Color INFO_BACKGROUND_COLOR = new Color(0.83, 0.93, 0.85, 1);

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
     * The last used background color
     */
    private Color lastBackgroundColor = Color.WHEAT;

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

        setVisible(false);

        autoHideDelayedRunnable = new DelayedRunnable(this::playHideAnimation, (long) autoHideDuration.toMillis());
    }

    /**
     * Shows an error notification. If a notification is already up then the text is
     * replaced and the hide timer is reset
     * 
     * @param text The text of the notification
     */
    public void showError(String text) {
        showText(text, ERROR_BACKGROUND_COLOR);
    }

    /**
     * Shows an info notification. If a notification is already up then the text is
     * * replaced and the hide timer is reset
     * 
     * @param text The text of the notification
     */
    public void showInfo(String text) {
        showText(text, INFO_BACKGROUND_COLOR);
    }

    /**
     * Shows the given text with the given background color. If a notification is
     * already up then the text is * replaced and the hide timer is reset
     * 
     * @param text  The text
     * @param color The color of the notification
     */
    private void showText(String text, Color color) {
        label.setText(text);
        if (!isVisible() || timeline.getStatus() != Status.STOPPED) {
            borderPane.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
            playShowAnimation();
        } else {
            playColorChangeAnimation(lastBackgroundColor, color);
        }
        setVisible(true);
        autoHideDelayedRunnable.run();

        lastBackgroundColor = color;
    }

    /**
     * Plays an animation changing from the old color the the new color
     * 
     * @param lastColor The previous color
     * @param newColor  The new color
     */
    private void playColorChangeAnimation(Color lastColor, Color newColor) {
        new ColorTransition(newColor, lastColor).play();
    }

    /**
     * Plays the animation for showing the notification
     */
    private void playShowAnimation() {
        timeline.stop();
        setTranslateY(borderPane.getHeight());
        timeline.getKeyFrames().setAll(createKeyFrame(Duration.ZERO, 0),
                createKeyFrame(TRANSLATE_ANIMATION_DURATION, -borderPane.getHeight()));
        timeline.play();
    }

    /**
     * Plays the animation for hiding the notification
     */
    private void playHideAnimation() {
        timeline.stop();
        timeline.getKeyFrames().setAll(createKeyFrame(Duration.ZERO, -borderPane.getHeight()),
                createKeyFrame(TRANSLATE_ANIMATION_DURATION, 0, false));
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

    /**
     * A transition which transitions between two colors
     */
    private class ColorTransition extends Transition {
        /**
         * The new color to transition to
         */
        private Color newColor;
        /**
         * The old color to transition from
         */
        private Color lastColor;

        /**
         * Constructor
         * <p>
         * The {@link #setCycleDuration(Duration)} is set to
         * {@link BottomNotificationPane#COLOR_ANIMATION_DURATION}
         * </p>
         * 
         * @param newColor  The new color to transition to
         * @param lastColor The old color to transition from
         */
        private ColorTransition(Color newColor, Color lastColor) {
            this.newColor = newColor;
            this.lastColor = lastColor;
            setCycleDuration(COLOR_ANIMATION_DURATION);
        }

        @Override
        protected void interpolate(double frac) {
            Color color = lastColor.interpolate(newColor, frac);
            var background = new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY));
            borderPane.setBackground(background);

        }
    }
}
