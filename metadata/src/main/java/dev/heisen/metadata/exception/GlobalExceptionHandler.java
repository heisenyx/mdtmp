package dev.heisen.metadata.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MetadataNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAggregate(MetadataNotFoundException ex, HttpServletRequest request) {
        log.warn("Publication metadata not found: {}", request.getRequestURI());

        ErrorResponse response = ErrorResponse.builder()
                .timestamp(Instant.now().toString())
                .status(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, HttpServletRequest request) {
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, ex, request);
    }

    private ResponseEntity<ErrorResponse> buildError(HttpStatus status,
                                                     Exception ex,
                                                     HttpServletRequest request) {
        ErrorResponse body = ErrorResponse.builder()
                .timestamp(Instant.now().toString())
                .status(status.value())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        log.error("Error occurred [{} {}]: {}", request.getMethod(), request.getRequestURI(), ex.getMessage(), ex);
        return ResponseEntity.status(status).body(body);
    }
}
