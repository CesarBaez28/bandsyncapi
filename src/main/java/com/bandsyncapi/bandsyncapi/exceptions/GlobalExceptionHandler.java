package com.bandsyncapi.bandsyncapi.exceptions;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.bandsyncapi.bandsyncapi.response.ApiResponse;

import jakarta.persistence.EntityNotFoundException;

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
        .body(new ApiResponse<>(false, "An unexpected error occurred", null, null));
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
    String errorMessage = ex.getMostSpecificCause().getMessage();
    final Map<String, String> constraintMessages = new HashMap<>();

    constraintMessages.put("musical_bands.name", "Ya existe una banda con ese nombre.");
    constraintMessages.put("musical_bands.email", "Ya existe una banda con ese mismo correo electrónico.");
    constraintMessages.put("musical_genres.musical_band_id", "El género ya está registrado para esta banda.");
    constraintMessages.put("musical_roles.musical_band_id", "Ese role musical ya está registrado para esta banda.");
    constraintMessages.put("artists.musical_band_id", "Ese artista ya está registrado para esta banda.");
    constraintMessages.put("repertoires.musical_band_id", "Ese repertorio ya está registrado para esta banda.");
    constraintMessages.put("users.email", "Ya existe un usuario con ese mismo correo electrónico.");
    constraintMessages.put("users.username", "Ya existe un usuario con ese nombre de usuario");

    String key = extractConstraintName(errorMessage);

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ApiResponse<>(false,
            constraintMessages.getOrDefault(key,
                "Ha ocurrido un error al violar una restricción de integridad de la base de datos."),
            null, null));
  }

  /**
   * Handle EntityNotFoundException
   * 
   * @param ex - Object EntityNotFoundException
   * @return - An object ApiResponse with the error
   */
  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ApiResponse<Void>> handleEntityNotFoundException(EntityNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, ex.getMessage(), null, null));
  }

  /**
   * Handle NoSuchElementException
   * 
   * @param ex - NoSuchElementException object
   * @return - An ApiResponse object with the error
   */
  @ExceptionHandler(NoSuchElementException.class)
  public ResponseEntity<ApiResponse<Void>> handleNotFoundException(NoSuchElementException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, ex.getMessage(), null, null));
  }

  /**
   * Handle UsernameNotFoundException
   * 
   * @param ex - Object UsernameNotFoundException
   * @return - An ApiResponse object with the error
   */
  @ExceptionHandler(UsernameNotFoundException.class)
  public ResponseEntity<ApiResponse<Void>> handleUserNotFoundException(UsernameNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, ex.getMessage(), null, null));
  }

  /**
   * Handle BadCredentialsException
   * 
   * @param ex - Object BadCredentialsException
   * @return - An ApiResponse object with the error
   */
  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<ApiResponse<Void>> handleBadCredentialsException(BadCredentialsException ex) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(false, "Credenciales incorrectas.", null, null));
  }

  /**
   * Extract the constrain name from the database
   * 
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
