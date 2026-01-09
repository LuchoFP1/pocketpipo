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
    public ResponseEntity<ApiErrorDTO> handleNotFound(
            DuplicatedEmailException ex,
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