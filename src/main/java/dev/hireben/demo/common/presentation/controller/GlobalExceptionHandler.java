package dev.hireben.demo.common.presentation.controller;

import java.io.IOException;
import java.util.Collection;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import dev.hireben.demo.common.presentation.dto.FieldValidationErrorMap;
import dev.hireben.demo.common.presentation.utility.context.RequestUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class GlobalExceptionHandler {

  // ---------------------------------------------------------------------------//
  // Methods
  // ---------------------------------------------------------------------------//

  @ExceptionHandler(MethodArgumentNotValidException.class)
  protected void handleFieldValidationException(
      MethodArgumentNotValidException exception,
      WebRequest request,
      HttpServletResponse response) throws IOException {

    Collection<ObjectError> validationErrors = exception.getBindingResult().getAllErrors();

    Collection<FieldValidationErrorMap> validationErrorMaps = validationErrors.stream()
        .map(validationError -> FieldValidationErrorMap.builder()
            .field(((FieldError) validationError).getField())
            .message(validationError.getDefaultMessage())
            .build())
        .toList();

    RequestUtil.setAttribute(request, "data", validationErrorMaps);

    response.sendError(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
  }

  // ---------------------------------------------------------------------------//

  @ExceptionHandler(ConstraintViolationException.class)
  protected void handleConstraintViolationException(
      ConstraintViolationException exception,
      WebRequest request,
      HttpServletResponse response) throws IOException {

    Collection<ConstraintViolation<?>> violationErrors = exception.getConstraintViolations();

    Collection<FieldValidationErrorMap> violationErrorMaps = violationErrors.stream()
        .map(violationError -> FieldValidationErrorMap.builder()
            .field(violationError.getPropertyPath().toString())
            .message(violationError.getMessage())
            .build())
        .toList();

    RequestUtil.setAttribute(request, null, violationErrorMaps);

    response.sendError(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
  }

  // ---------------------------------------------------------------------------//

  @ExceptionHandler(MissingRequestHeaderException.class)
  protected void handleMissingRequestHeaderException(
      MissingRequestHeaderException exception,
      HttpServletRequest request,
      HttpServletResponse response) throws IOException {

    response.sendError(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
  }

  // ---------------------------------------------------------------------------//

  @ExceptionHandler(HttpMessageNotReadableException.class)
  protected void handleHttpMessageNotReadableException(
      HttpMessageNotReadableException exception,
      HttpServletRequest request,
      HttpServletResponse response) throws IOException {

    response.sendError(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
  }

  // ---------------------------------------------------------------------------//

  @ExceptionHandler(NoResourceFoundException.class)
  protected void handleNoResourceFoundException(
      NoResourceFoundException exception,
      HttpServletRequest request,
      HttpServletResponse response) throws IOException {

    response.sendError(HttpStatus.NOT_FOUND.value(), exception.getMessage());
  }

  // ---------------------------------------------------------------------------//

  @ExceptionHandler(Exception.class)
  protected void handleException(
      Exception exception,
      HttpServletRequest request,
      HttpServletResponse response) throws IOException {

    response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage());
  }

}
