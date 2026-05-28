package com.bootcamp.onlineschool.exception;

/**
 * Exception thrown when a user attempts to access a resource without proper authorization.
 */
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
}
