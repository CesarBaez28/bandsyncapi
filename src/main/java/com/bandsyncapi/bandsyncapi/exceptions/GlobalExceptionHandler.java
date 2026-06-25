package com.bandsyncapi.bandsyncapi.exceptions;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.bandsyncapi.bandsyncapi.response.ApiResponse;

import io.jsonwebtoken.ExpiredJwtException;
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
    constraintMessages.put("users.username", "Ya existe un usuario registrado con ese nombre.");

    String key = extractConstraintName(errorMessage);

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ApiResponse<>(false,
            constraintMessages.getOrDefault(key,
                ex.getMessage()),
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
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body(new ApiResponse<>(false, "Bad credentials.", null, null));
  }

  /**
   * Handle FileStorageException
   * 
   * @param ex - FileStorageException object
   * @return - An ApiResponse object with the error
   */
  @ExceptionHandler(FileStorageException.class)
  public ResponseEntity<ApiResponse<Void>> handleFileStorageException(FileStorageException ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ApiResponse<>(false, ex.getMessage(), null, null));
  }

  /**
   * handle MaxUploadSizeExceededException
   * 
   * @param ex - MaxUploadSizeExceededException object
   * @return An ApiResponse object with the error
   */
  @ExceptionHandler(MaxUploadSizeExceededException.class)
  public ResponseEntity<ApiResponse<Void>> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ApiResponse<>(false, "El archivo sobrepasó el tamaño máximo permitido", null, null));
  }

  /**
   * IllegalArgumentException
   * 
   * @param ex - IllegalArgumentException object
   * @return - An ApiResponse object with the error
   */
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(IllegalArgumentException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, ex.getMessage(), null, null));
  }

  /**
   * IllegalStateException
   * 
   * @param ex - IllegalStateException object
   * @return - An ApiResponse object with the error
   */
  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<ApiResponse<Void>> handleIllegalStateException(IllegalStateException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, ex.getMessage(), null, null));
  }

  /**
   * ExpiredJwtException
   * 
   * @param ex - ExpiredJwtException object
   * @return - An ApiResponse object with the error
   */
  @ExceptionHandler(ExpiredJwtException.class)
  public ResponseEntity<ApiResponse<Void>> handleExpiredJwtException(ExpiredJwtException ex) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body(new ApiResponse<>(false, "The token has expired.", null, null));
  }

  /**
   * InvitationException
   * 
   * @param ex - InvitationException object
   * @return An ApiResponse object with the error
   */
  @ExceptionHandler(InvitationException.class)
  public ResponseEntity<ApiResponse<Void>> handleInvitationException(InvitationException ex) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body(new ApiResponse<>(false, ex.getMessage(), null, null));
  }

  /**
   * AuthorizationDeniedException
   * 
   * @param ex - AuthorizationDeniedException object
   * @return - An ApiResponse object with the error
   */
  @ExceptionHandler(AuthorizationDeniedException.class)
  public ResponseEntity<ApiResponse<Void>> handleAuthorizationDeniedException(AuthorizationDeniedException ex) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN)
        .body(new ApiResponse<>(false, ex.getMessage(), null, null));
  }

  /**
   * Handle ResetPasswordException
   * 
   * @param ex - ResetPasswordException object
   * @return - An ApiResponse object with the error
   */
  @ExceptionHandler(ResetPasswordException.class)
  public ResponseEntity<ApiResponse<Void>> handleResetPasswordException(ResetPasswordException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ApiResponse<>(false, ex.getMessage(), null, null));
  }

  /**
   * Handle RateLimitExceededException
   * 
   * @param ex - RateLimitExceededException object
   * @return An ApiResponse object with the error
   */
  @ExceptionHandler(RateLimitExceededException.class)
  public ResponseEntity<ApiResponse<Void>> handleRateLimitExceededException(RateLimitExceededException ex) {
    return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
        .body(new ApiResponse<>(false, ex.getMessage(), null, null));
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
