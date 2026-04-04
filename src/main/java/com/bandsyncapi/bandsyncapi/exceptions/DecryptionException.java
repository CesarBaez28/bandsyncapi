package com.bandsyncapi.bandsyncapi.exceptions;

/**
 * Custom exception to handle errors when decrypting data
 */
public class DecryptionException extends RuntimeException {

  /*
   * Constructor for DecryptionException
   * 
   * @param message - the error message
   */
  public DecryptionException(String message) {
    super(message);
  }

  /*
   * Constructor for DecryptionException with cause
   * 
   * @param message - the error message
   * 
   * @param cause - the cause of the exception
   */
  public DecryptionException(String message, Throwable cause) {
    super(message, cause);
  }

}
