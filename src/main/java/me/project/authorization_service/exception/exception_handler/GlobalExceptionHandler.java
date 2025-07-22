package me.project.authorization_service.exception.exception_handler;

import me.project.authorization_service.exception.InvalidRefreshTokenException;
import me.project.authorization_service.exception.InvalidUsernameException;
import me.project.authorization_service.exception.UserNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidUsernameException.class)
    public ResponseEntity<String> handleInvalidUsernameException(final InvalidUsernameException e) {
        return ResponseEntity.status(400).body(e.getMessage());
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<String> handleInvalidRefreshTokenException(final InvalidRefreshTokenException e) {
        return ResponseEntity.status(401).body(e.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(final UserNotFoundException e) {
        return ResponseEntity.status(404).body(e.getMessage());
    }

}
