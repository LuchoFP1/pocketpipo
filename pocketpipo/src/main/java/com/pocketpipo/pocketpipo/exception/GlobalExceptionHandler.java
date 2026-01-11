package com.pocketpipo.pocketpipo.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.pocketpipo.pocketpipo.dto.ApiErrorDTO;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicatedEmailException.class)
    public ResponseEntity<ApiErrorDTO> handleNotFound(
            DuplicatedEmailException ex,
            HttpServletRequest req
    ) {
        ApiErrorDTO body = new ApiErrorDTO(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                req.getRequestURI(),
                Instant.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

     @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleAuth(AuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                Map.of(
                        "timestamp", LocalDateTime.now().toString(),
                        "status", 401,
                        "error", "Unauthorized",
                        "message", ex.getClass().getSimpleName() + ": " + ex.getMessage()
                )
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiErrorDTO> handleNotFound(
            UserNotFoundException ex,
            HttpServletRequest req
    ) {
        ApiErrorDTO body = new ApiErrorDTO(
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                req.getRequestURI(),
                Instant.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }
}