package com.bandsyncapi.bandsyncapi.exceptions;

/**
 * Custom exception to handle reset password related errors
 */
public class ResetPasswordException extends RuntimeException {

  /**
   * Constructor for ResetPasswordException with a message
   * 
   * @param message - The error message describing the exception
   */
  public ResetPasswordException(String message) {
    super(message);
  }

  /**
   * Constructor for ResetPasswordException with a message and a cause
   * 
   * @param message - The error message describing the exception
   * @param cause   - The underlying cause of the exception
   */
  public ResetPasswordException(String message, Throwable cause) {
    super(message, cause);
  }
}
