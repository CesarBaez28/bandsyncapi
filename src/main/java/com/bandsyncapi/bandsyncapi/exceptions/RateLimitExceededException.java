package com.bandsyncapi.bandsyncapi.exceptions;

/**
 * Custom exception related to rate limit
 */
public class RateLimitExceededException extends RuntimeException {

  public RateLimitExceededException(String message) {
    super(message);
  }

  public RateLimitExceededException(String message, Throwable cause) {
    super(message, cause);
  }
}
