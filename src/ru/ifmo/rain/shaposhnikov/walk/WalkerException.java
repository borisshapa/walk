package ru.ifmo.rain.shaposhnikov.walk;

public class WalkerException extends Exception {
    public WalkerException(final String errorMessage) {
        super(errorMessage);
    }

    public WalkerException(final String errorMessage, final Throwable cause) {
        super(errorMessage, cause);
    }
}
