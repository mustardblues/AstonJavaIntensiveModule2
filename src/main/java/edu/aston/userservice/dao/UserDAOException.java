package edu.aston.userservice.dao;

public class UserDAOException extends Exception {
    public UserDAOException(final String message) {
        super(message);
    }

    public UserDAOException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
