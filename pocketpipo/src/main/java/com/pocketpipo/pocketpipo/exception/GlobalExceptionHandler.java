package com.pocketpipo.pocketpipo.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.pocketpipo.pocketpipo.dto.ApiErrorDTO;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicatedEmailException.class)
    public ResponseEntity<ApiErrorDTO> handleDuplicatedEmail(
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
public ResponseEntity<ApiErrorDTO> handleAuth(
        AuthenticationException ex,
        HttpServletRequest req
) {
    ApiErrorDTO body = new ApiErrorDTO(
            ex.getMessage(),
            HttpStatus.UNAUTHORIZED.value(),
            req.getRequestURI(),
            Instant.now()
    );
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
}

    @ExceptionHandler(ExpenseNotFoundException.class)
public ResponseEntity<ApiErrorDTO> handleExpenseNotFound(
        ExpenseNotFoundException ex,
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

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiErrorDTO> handleUserNotFound(
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

    @ExceptionHandler(InvalidDateException.class)
public ResponseEntity<ApiErrorDTO> handleInvalidDate(
        InvalidDateException ex,
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
}