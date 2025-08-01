package dev.heisen.enhancement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class EnhanceFailedException extends RuntimeException {
    public EnhanceFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
