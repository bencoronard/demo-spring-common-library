package dev.hireben.demo.common.domain.exception;

public abstract class DomainException extends RuntimeException {

  // ---------------------------------------------------------------------------//
  // Constructors
  // ---------------------------------------------------------------------------//

  protected DomainException(String message) {
    super(message);
  }

}
