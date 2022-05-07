package ch.zhaw.ikitomo.exception;

/**
 * This exception is throws when a critical animation is missing from the files
 * or definition. Typically, this is the first {@link StateType#IDLE}
 * animation
 */
public class MissingAnimationException extends Exception {
    /**
     * Constructor
     * 
     * @param message the exception message
     */
    public MissingAnimationException(String message) {
        super(message);
    }

    /**
     * Constructor
     * 
     * @param message the exception message
     * @param cause   the cause of the exception
     */
    public MissingAnimationException(String message, Throwable cause) {
        super(message, cause);
    }
}
