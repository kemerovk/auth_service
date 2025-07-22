package me.project.authorization_service.exception;

public class InvalidUsernameException extends RuntimeException {
    public InvalidUsernameException(String s) {
        super(s);
    }
}
