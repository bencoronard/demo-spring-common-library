package dev.hireben.demo.common_libs.http.handler;

import java.net.SocketTimeoutException;
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

import dev.hireben.demo.common_libs.http.dto.FieldValidationErrorMap;
import io.micrometer.tracing.Tracer;
import jakarta.validation.ConstraintViolationException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  private final Tracer tracer;

  @Override
  protected final @NonNull ResponseEntity<Object> createResponseEntity(
      @Nullable Object body,
      @NonNull HttpHeaders headers,
      @NonNull HttpStatusCode statusCode,
      @NonNull WebRequest request) {

    if (body instanceof ProblemDetail problemDetail) {
      problemDetail.setProperty("timestamp", Instant.now());
      problemDetail.setProperty("trace", tracer.currentTraceContext().context().traceId());
    }

    return super.createResponseEntity(body, headers, statusCode, request);
  }

  // =============================================================================

  @Override
  protected final ResponseEntity<Object> handleMethodArgumentNotValid(
      @NonNull MethodArgumentNotValidException ex,
      @NonNull HttpHeaders headers,
      @NonNull HttpStatusCode status,
      @NonNull WebRequest request) {

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
