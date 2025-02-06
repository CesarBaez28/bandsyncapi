package com.bandsyncapi.bandsyncapi.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
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
   * @return - An object ApiResponse with the error
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse<Void>> handleGeneralException() {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ApiResponse<>(false, "An unexpected error occurs", null, null));
  }

  /**
   * Handle field validations errors
   * 
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

  /**
   * Handle integrity violation exception
   * 
   * @return - An object ApiResponse with the error
   */
  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ApiResponse<Void>> handleIntegrityViolationException(DataIntegrityViolationException ex) {
    String errorMesage = ex.getMostSpecificCause().getMessage();
    final Map<String, String> CONSTRAINT_MESSAGES = new HashMap<>();

    CONSTRAINT_MESSAGES.put("musical_bands.name", "Ya existe una banda con ese nombre.");
    CONSTRAINT_MESSAGES.put("musical_genres.musical_band_id", "El género ya está registrado para esta banda.");

    String key = extractConstraintName(errorMesage);

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ApiResponse<>(false,
            CONSTRAINT_MESSAGES.getOrDefault(key, "Ya existe un registro con ese nombre"), null, null));
  }

  /**
   * Extract the constrain name from the database
   * @param errorMessage - The error message from the exception
   * @return - A string with the constrain name
   */
  private String extractConstraintName(String errorMessage) {
    if (errorMessage.contains("Duplicate entry")) {
      int startIndex = errorMessage.indexOf("for key '") + 9;
      int endIndex = errorMessage.indexOf("'", startIndex);
      return errorMessage.substring(startIndex, endIndex);
    }
    return "unknown_constraint";
  }
}
