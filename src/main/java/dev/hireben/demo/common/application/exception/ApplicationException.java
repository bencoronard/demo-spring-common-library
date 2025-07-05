package dev.hireben.demo.common.application.exception;

public abstract class ApplicationException extends RuntimeException {

  // ---------------------------------------------------------------------------//
  // Constructors
  // ---------------------------------------------------------------------------//

  protected ApplicationException(String message) {
    super(message);
  }

}
