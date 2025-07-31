package dev.heisen.aggregator.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AggregateException extends RuntimeException {
    public AggregateException(String message) {
        super(message);
    }
}
