package ch.zhaw.ikitomo.common;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Decorates a runnable with a delay. When the runnable is called again the
 * timer for the delay is reset.
 */
public class DelayedRunnable implements Runnable {
    /**
     * The logger
     */
    private static final Logger LOGGER = Logger.getLogger(DelayedRunnable.class.getName());
    /**
     * The global timer for delayed runnable instances
     */
    private Timer timer = new Timer(DelayedRunnable.class.getName() + " timer", true);
    /**
     * The runnable to decorate
     */
    private Runnable runnable;

    /**
     * The delay after the last call of the runnable before {@link #runnable} is
     * called
     */
    private long delayInMs;

    /**
     * the last timer task
     */
    private TimerTask lastTask = null;

    /**
     * Constructor
     * 
     * @param runnable  the runnable to decorate
     * @param delayInMs the delay in ms
     * @throws IllegalArgumentException if the delay is negative
     * @throws NullPointerException     if the runnable is null
     */
    public DelayedRunnable(Runnable runnable, long delayInMs) {
        this.runnable = Objects.requireNonNull(runnable);
        this.delayInMs = delayInMs;
        if (delayInMs < 0) {
            throw new IllegalArgumentException("delayInMs must be >= 0");
        }
    }

    @Override
    public void run() {
        if (lastTask != null) {
            lastTask.cancel();
        }
        lastTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "The runnable threw an exception: ", e);
                }
            }
        };
        timer.schedule(lastTask, delayInMs);
    }

    /**
     * Gets the delay in ms between the last call to {@link #run()} and calling
     * {@link #runnable#run()}
     * 
     * @return the delay in ms
     */
    public long getDelayInMs() {
        return delayInMs;
    }
}
