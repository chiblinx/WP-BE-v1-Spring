package com.chiblinx.core.exceptions;

import com.chiblinx.core.responses.ApiErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionsHandler {

  @ExceptionHandler(Exception.class)
  ResponseEntity<ApiErrorResponse> generalExceptionsHandler(Exception ex) {
    ApiErrorResponse error = new ApiErrorResponse(ex.getMessage());
    return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(AccessDeniedException.class)
  ResponseEntity<ApiErrorResponse> handleAccessDeniedException(
      AccessDeniedException exception) {
    ApiErrorResponse error = new ApiErrorResponse(exception.getMessage(), HttpStatus.FORBIDDEN);
    return new ResponseEntity<>(error, error.getStatus());
  }

  @ExceptionHandler(BadCredentialsException.class)
  ResponseEntity<ApiErrorResponse> handleBadCredentialsException(
      BadCredentialsException exception) {
    ApiErrorResponse error = new ApiErrorResponse(exception.getMessage(), HttpStatus.UNAUTHORIZED);
    return new ResponseEntity<>(error, error.getStatus());
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  ResponseEntity<ApiErrorResponse> handleDataIntegrityViolationException(
      DataIntegrityViolationException exception) {
    ApiErrorResponse error = new ApiErrorResponse("failed, conflict error occurred",
        HttpStatus.CONFLICT);
    return new ResponseEntity<>(error, error.getStatus());
  }

  @ExceptionHandler(ExpiredJwtException.class)
  ResponseEntity<ApiErrorResponse> handleExpiredJwtException(ExpiredJwtException exception) {
    ApiErrorResponse error = new ApiErrorResponse(exception.getMessage(), HttpStatus.UNAUTHORIZED);
    return new ResponseEntity<>(error, error.getStatus());
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  ResponseEntity<ApiErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
    ApiErrorResponse error = new ApiErrorResponse(exception.getMessage(), HttpStatus.BAD_REQUEST);
    return new ResponseEntity<>(error, error.getStatus());
  }

  @ExceptionHandler(InternalAuthenticationServiceException.class)
  ResponseEntity<ApiErrorResponse> handleInternalAuthenticationServiceException(
      InternalAuthenticationServiceException exception) {
    ApiErrorResponse error = new ApiErrorResponse(exception.getMessage(), HttpStatus.UNAUTHORIZED);
    return new ResponseEntity<>(error, error.getStatus());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException exception) {
    Map<String, String> errors = new HashMap<>();

    exception.getBindingResult().getFieldErrors().forEach(field -> {
      String fieldName = field.getField();
      String errorMessage = field.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });

    var error = new ApiErrorResponse(errors.toString(), HttpStatus.BAD_REQUEST);
    return new ResponseEntity<>(error, error.getStatus());
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  ResponseEntity<ApiErrorResponse> handleMissingServletRequestParameterException(
      MissingServletRequestParameterException exception) {
    ApiErrorResponse error = new ApiErrorResponse(exception.getMessage(), HttpStatus.BAD_REQUEST);
    return new ResponseEntity<>(error, error.getStatus());
  }

  @ExceptionHandler(NoResourceFoundException.class)
  ResponseEntity<ApiErrorResponse> handleNoResourceFoundException(
      NoResourceFoundException exception) {
    ApiErrorResponse error = new ApiErrorResponse(exception.getMessage(), HttpStatus.NOT_FOUND);
    return new ResponseEntity<>(error, error.getStatus());
  }

  @ExceptionHandler(NullPointerException.class)
  ResponseEntity<ApiErrorResponse> handleNullPointerException(NullPointerException exception) {
    ApiErrorResponse error = new ApiErrorResponse(exception.getMessage(), HttpStatus.BAD_REQUEST);
    return new ResponseEntity<>(error, error.getStatus());
  }

  @ExceptionHandler(ConstraintViolationException.class)
  ResponseEntity<ApiErrorResponse> handleConstraintViolationException(
      ConstraintViolationException exception) {
    ArrayList<String> errors = new ArrayList<>();

    exception.getConstraintViolations().forEach(item -> errors.add(item.getMessage()));

    ApiErrorResponse error = new ApiErrorResponse(errors.toString(), HttpStatus.BAD_REQUEST);
    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(BadRequestException.class)
  ResponseEntity<ApiErrorResponse> handleBadRequestException(BadRequestException exception) {
    ApiErrorResponse error = new ApiErrorResponse(exception.getMessage(), exception.statusCode);
    return new ResponseEntity<>(error, exception.statusCode);
  }

  @ExceptionHandler(ConflictException.class)
  ResponseEntity<ApiErrorResponse> handleConflictException(ConflictException exception) {
    ApiErrorResponse error = new ApiErrorResponse(exception.getMessage(), exception.statusCode);
    return new ResponseEntity<>(error, exception.statusCode);
  }

  @ExceptionHandler(ForbiddenException.class)
  ResponseEntity<ApiErrorResponse> handleNotForbiddenException(ForbiddenException exception) {
    ApiErrorResponse error = new ApiErrorResponse(exception.getMessage(), exception.statusCode);
    return new ResponseEntity<>(error, exception.statusCode);
  }

  @ExceptionHandler(InternalServerException.class)
  ResponseEntity<ApiErrorResponse> handleInternalServerException(
      InternalServerException exception) {
    ApiErrorResponse error = new ApiErrorResponse(exception.getMessage(), exception.statusCode);
    return new ResponseEntity<>(error, exception.statusCode);
  }

  @ExceptionHandler(NotFoundException.class)
  ResponseEntity<ApiErrorResponse> handleNotFoundException(NotFoundException exception) {
    ApiErrorResponse error = new ApiErrorResponse(exception.getMessage(), exception.statusCode);
    return new ResponseEntity<>(error, exception.statusCode);
  }

  @ExceptionHandler(UnauthorizedException.class)
  ResponseEntity<ApiErrorResponse> handleUnauthorizedException(UnauthorizedException exception) {
    ApiErrorResponse error = new ApiErrorResponse(exception.getMessage(), exception.statusCode);
    return new ResponseEntity<>(error, exception.statusCode);
  }

}
