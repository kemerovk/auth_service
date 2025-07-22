package me.project.authorization_service.exception;

public class UserAlreadyExistsException extends RuntimeException{
    public UserAlreadyExistsException(String msg) {
        super(msg);
    }
}
