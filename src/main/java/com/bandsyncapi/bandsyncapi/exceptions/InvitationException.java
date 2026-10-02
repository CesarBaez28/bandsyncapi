package com.bandsyncapi.bandsyncapi.exceptions;

/**
 * Custom exception to handle invitation related errors
 */
public class InvitationException extends RuntimeException {

  public InvitationException(String message) {
    super(message);
  }

  public InvitationException(String message, Throwable cause) {
    super(message, cause);
  }
}
