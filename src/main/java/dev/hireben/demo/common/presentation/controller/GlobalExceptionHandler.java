package dev.hireben.demo.common.presentation.controller;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import dev.hireben.demo.common.presentation.dto.FieldValidationErrorMap;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@SuppressWarnings("unused")
public class GlobalExceptionHandler {

  // ---------------------------------------------------------------------------//
  // Fields
  // ---------------------------------------------------------------------------//

  private static final Map<Class<? extends Throwable>, HttpStatus> EXCEPTION_STATUS_MAP = Map.of();

  private static final Map<Class<? extends Throwable>, String> EXCEPTION_CODE_MAP = Map.of();

  // ---------------------------------------------------------------------------//
  // Methods
  // ---------------------------------------------------------------------------//

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public void handleFieldValidationException(
      MethodArgumentNotValidException exception,
      HttpServletRequest request,
      HttpServletResponse response) throws IOException {

    Collection<ObjectError> validationErrors = exception.getBindingResult().getAllErrors();

    Collection<FieldValidationErrorMap> validationErrorMaps = validationErrors.stream()
        .map(validationError -> FieldValidationErrorMap.builder()
            .field(((FieldError) validationError).getField())
            .message(validationError.getDefaultMessage())
            .build())
        .toList();

    response.sendError(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
  }

  // ---------------------------------------------------------------------------//

  @ExceptionHandler(ConstraintViolationException.class)
  public void handleConstraintViolationException(
      ConstraintViolationException exception,
      HttpServletRequest request,
      HttpServletResponse response) throws IOException {

    Collection<ConstraintViolation<?>> violationErrors = exception.getConstraintViolations();

    Collection<FieldValidationErrorMap> violationErrorMaps = violationErrors.stream()
        .map(violationError -> FieldValidationErrorMap.builder()
            .field(violationError.getPropertyPath().toString())
            .message(violationError.getMessage())
            .build())
        .toList();

    response.sendError(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
  }

  // ---------------------------------------------------------------------------//

  @ExceptionHandler(MissingRequestHeaderException.class)
  public void handleMissingRequestHeaderException(
      MissingRequestHeaderException exception,
      HttpServletRequest request,
      HttpServletResponse response) throws IOException {

    response.sendError(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
  }

  // ---------------------------------------------------------------------------//

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public void handleHttpMessageNotReadableException(
      HttpMessageNotReadableException exception,
      HttpServletRequest request,
      HttpServletResponse response) throws IOException {

    response.sendError(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
  }

  // ---------------------------------------------------------------------------//

  @ExceptionHandler(NoResourceFoundException.class)
  public void handleNoResourceFoundException(
      NoResourceFoundException exception,
      HttpServletRequest request,
      HttpServletResponse response) throws IOException {

    response.sendError(HttpStatus.NOT_FOUND.value(), exception.getMessage());
  }

  // ---------------------------------------------------------------------------//

  @ExceptionHandler(Exception.class)
  public void handleException(
      Exception exception,
      HttpServletRequest request,
      HttpServletResponse response) throws IOException {

    response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage());
  }

}
