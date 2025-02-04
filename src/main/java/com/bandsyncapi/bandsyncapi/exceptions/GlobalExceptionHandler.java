package com.bandsyncapi.bandsyncapi.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.bandsyncapi.bandsyncapi.response.ApiResponse;

/**
 * Globally handle all exceptions in the API
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Handle general exceptions
   * 
   * @param ex - Exception
   * @return - An object ApiResponse with the error
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse<Void>> handleGeneralException(Exception ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ApiResponse<>(false, "An unexpected error occurs", null, null));
  }

  /**
   * Handle field validations errors
   * @param ex - Exception
   * @return - An object ApiResponse with the field errors
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

    ApiResponse<Map<String, String>> response = new ApiResponse<>(
        false,
        "Las validaciones de los campos fallaron.",
        null,
        errors);

    return ResponseEntity.badRequest().body(response);
  }
}
