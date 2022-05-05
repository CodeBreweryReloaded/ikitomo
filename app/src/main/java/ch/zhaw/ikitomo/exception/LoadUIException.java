package ch.zhaw.ikitomo.exception;

/**
 * An exception thrown when a gui can't be loaded.
 * <p>
 * If this is thrown something
 * is wrong with the code and a caller isn't expected to handle it. Thus it is a
 * runtime exception.
 * </p>
 */
public class LoadUIException extends RuntimeException {

    /**
     * Constructor
     * 
     * @param message the exception message
     */
    public LoadUIException(String message) {
        super(message);
    }

    /**
     * Constructor
     * 
     * @param message the exception message
     * @param cause   the cause of the exception
     */
    public LoadUIException(String message, Throwable cause) {
        super(message, cause);
    }
}
