package dev.hireben.demo.common.http.handler;

import java.net.SocketTimeoutException;
import java.time.Instant;
import java.util.Collection;
import org.jspecify.annotations.Nullable;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import dev.hireben.demo.common.http.dto.FieldValidationErrorMap;
import jakarta.validation.ConstraintViolationException;

public abstract class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @Override
  protected final ResponseEntity<Object> createResponseEntity(
      @Nullable Object body,
      HttpHeaders headers,
      HttpStatusCode statusCode,
      WebRequest request) {

    if (body instanceof ProblemDetail problemDetail) {
      problemDetail.setProperty("timestamp", Instant.now());
      // TODO Add trace from io.micrometer
      problemDetail.setProperty("trace", null);
    }

    return super.createResponseEntity(body, headers, statusCode, request);
  }

  // =============================================================================

  @Override
  protected final ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {

    ProblemDetail problemDetail = ex.updateAndGetBody(getMessageSource(), LocaleContextHolder.getLocale());

    Collection<FieldValidationErrorMap> errors = ex.getBindingResult().getAllErrors().stream()
        .map(error -> FieldValidationErrorMap.builder()
            .field(((FieldError) error).getField())
            .message(error.getDefaultMessage())
            .build())
        .toList();

    problemDetail.setProperty("errors", errors);

    return createResponseEntity(problemDetail, headers, status, request);
  }

  // -----------------------------------------------------------------------------

  @ExceptionHandler(ConstraintViolationException.class)
  private ResponseEntity<Object> handleConstraintViolation(
      ConstraintViolationException ex,
      WebRequest request) {

    HttpStatus status = HttpStatus.BAD_REQUEST;

    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, "Validation failed");

    Collection<FieldValidationErrorMap> errors = ex.getConstraintViolations().stream()
        .map(error -> FieldValidationErrorMap.builder()
            .field(error.getPropertyPath().toString())
            .message(error.getMessage())
            .build())
        .toList();

    problemDetail.setProperty("errors", errors);

    return createResponseEntity(problemDetail, HttpHeaders.EMPTY, status, request);
  }

  // -----------------------------------------------------------------------------

  @ExceptionHandler(MissingRequestHeaderException.class)
  private ResponseEntity<Object> handleMissingRequestHeader(
      MissingRequestHeaderException ex,
      WebRequest request) {

    HttpStatus status = HttpStatus.BAD_REQUEST;

    String message = String.format("Missing HTTP header: %s", ex.getHeaderName());

    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, message);

    return createResponseEntity(problemDetail, HttpHeaders.EMPTY, status, request);
  }

  // -----------------------------------------------------------------------------

  @ExceptionHandler(SocketTimeoutException.class)
  private ResponseEntity<Object> handleSocketTimeout(
      SocketTimeoutException ex,
      WebRequest request) {

    HttpStatus status = HttpStatus.BAD_REQUEST;

    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, "Gateway timed out");

    return createResponseEntity(problemDetail, HttpHeaders.EMPTY, status, request);
  }

}
