package ru.ifmo.rain.shaposhnikov.walk;

/**
 * Error crawling files.
 *
 * @author Boris Shaposhnikov
 */
public class WalkerException extends Exception {
    /**
     * Creates an error by message about the error.
     *
     * @param errorMessage error information
     */
    public WalkerException(final String errorMessage) {
        super(errorMessage);
    }

    /**
     * Creates an error by message about the error and {@link Throwable} object.
     *
     * @param errorMessage error information
     * @param cause        the reason the error was caught.
     */
    public WalkerException(final String errorMessage, final Throwable cause) {
        super(errorMessage, cause);
    }
}
