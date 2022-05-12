package ch.zhaw.ikitomo.behavior;

import java.util.Random;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.random.RandomGenerator;

import ch.zhaw.ikitomo.common.Vector2;
import ch.zhaw.ikitomo.overlay.model.TomodachiModel;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

/**
 * A class which will return a random position. This same position is returned
 * until the Tomodachi walks into a radius around the position after which it
 * will change again
 */
public class RandomNextPositionStrategy implements NextPositionStrategy {
    /**
     * The distance the tomodachi has to have to the last position until a new
     * position is generated
     */
    private static final double DISTANCE_TO_CALCULATE_NEW_POSITION = 50;

    /**
     * The logger
     */
    private static final Logger LOGGER = Logger.getLogger(RandomNextPositionStrategy.class.getName());

    /**
     * The random generator
     */
    private RandomGenerator random = new Random();
    /**
     * The last position which was returned
     */
    private Vector2 lastPosition = null;

    private Supplier<Rectangle2D> screenBoundsSupplier = () -> Screen.getPrimary().getBounds();

    /**
     * Constructor
     */
    public RandomNextPositionStrategy() {
    }

    /**
     * Sets the random generator. This is used for testing
     * 
     * @param random The new random generator
     */
    void setRandom(RandomGenerator random) {
        this.random = random;
    }

    @Override
    public Vector2 nextPosition(TomodachiModel model) {
        if (lastPosition == null) {
            calculateRandomPosition();
        }
        double distance = model.getPosition().subtract(lastPosition).absolute();
        if (distance < DISTANCE_TO_CALCULATE_NEW_POSITION || !lastPositionInBounds()) {
            calculateRandomPosition();
        }
        return lastPosition;
    }

    /**
     * Checks if {@link #lastPosition} is still in bound of the screen
     * 
     * @return If the position is still in bounds
     */
    private boolean lastPositionInBounds() {
        Rectangle2D bounds = screenBoundsSupplier.get();
        return bounds.contains(lastPosition.x(), lastPosition.y());
    }

    /**
     * Calculates a random position in bounds of the current primary screen
     * 
     * @return The newly calculated position
     */
    private void calculateRandomPosition() {
        Rectangle2D screenBounds = screenBoundsSupplier.get();
        double x = random.nextDouble(screenBounds.getMinX(), screenBounds.getMaxX());
        double y = random.nextDouble(screenBounds.getMinY(), screenBounds.getMaxY());
        lastPosition = new Vector2(x, y);
        LOGGER.log(Level.INFO, "Calculated new random position: {0}", lastPosition);
    }
}
