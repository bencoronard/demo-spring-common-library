package dev.hireben.demo.common.presentation.controller;

import java.time.Instant;
import java.util.Collection;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import dev.hireben.demo.common.presentation.dto.FieldValidationErrorMap;
import dev.hireben.demo.common.presentation.utility.context.HttpHeaderKey;
import jakarta.validation.ConstraintViolationException;

public abstract class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  // ---------------------------------------------------------------------------//
  // Methods
  // ---------------------------------------------------------------------------//

  @NonNull
  @Override
  protected ResponseEntity<Object> createResponseEntity(
      @Nullable Object body,
      @NonNull HttpHeaders headers,
      @NonNull HttpStatusCode statusCode,
      @NonNull WebRequest request) {

    if (body instanceof ProblemDetail problemDetail) {
      problemDetail.setProperty("timestamp", Instant.now());
      problemDetail.setProperty("traceId", request.getHeader(HttpHeaderKey.TRACE_ID));
    }

    return super.createResponseEntity(body, headers, statusCode, request);
  }

  // ---------------------------------------------------------------------------//

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      @NonNull MethodArgumentNotValidException ex,
      @NonNull HttpHeaders headers,
      @NonNull HttpStatusCode status,
      @NonNull WebRequest request) {

    ProblemDetail problemDetail = ex.updateAndGetBody(getMessageSource(), LocaleContextHolder.getLocale());

    Collection<FieldValidationErrorMap> errors = ex.getBindingResult().getAllErrors()
        .stream()
        .map(error -> FieldValidationErrorMap.builder()
            .field(((FieldError) error).getField())
            .message(error.getDefaultMessage())
            .build())
        .toList();

    problemDetail.setProperty("errors", errors);

    return createResponseEntity(problemDetail, headers, status, request);
  }

  // ---------------------------------------------------------------------------//

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Object> handleConstraintViolationException(
      ConstraintViolationException ex,
      WebRequest request) {

    HttpStatus status = HttpStatus.BAD_REQUEST;

    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
        status,
        "Validation failed");

    Collection<FieldValidationErrorMap> errors = ex.getConstraintViolations()
        .stream()
        .map(error -> FieldValidationErrorMap.builder()
            .field(error.getPropertyPath().toString())
            .message(error.getMessage())
            .build())
        .toList();

    problemDetail.setProperty("errors", errors);

    return createResponseEntity(problemDetail, HttpHeaders.EMPTY, status, request);
  }

  // ---------------------------------------------------------------------------//

  @ExceptionHandler(MissingRequestHeaderException.class)
  public ResponseEntity<Object> handleMissingRequestHeaderException(
      MissingRequestHeaderException ex,
      WebRequest request) {

    HttpStatus status = HttpStatus.BAD_REQUEST;

    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
        status,
        String.format("Missing HTTP header: %s", ex.getHeaderName()));

    return createResponseEntity(problemDetail, HttpHeaders.EMPTY, status, request);
  }

}
