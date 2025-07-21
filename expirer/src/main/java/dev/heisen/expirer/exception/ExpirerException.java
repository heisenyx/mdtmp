package dev.heisen.expirer.exception;

public class ExpirerException extends RuntimeException {
  public ExpirerException(String message) {
    super(message);
  }

  public ExpirerException(String message, Throwable cause) {
    super(message, cause);
  }
}
