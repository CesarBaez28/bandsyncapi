package com.bandsyncapi.bandsyncapi.response;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * Object that represents de response to be sent to the client
 */
@Data
public class ApiResponse<T> {
  private T errors;
  private boolean success;
  private String message;
  private LocalDateTime timestamp;
  private T data;

  /**
   * Constructor for the response
   * @param success - indicates if the response is successful
   * @param message - response message
   * @param data - Data to be sent to the client (null if error)
   * @param errors - Errors to be sent to the client (null if success)
   */
  public ApiResponse(Boolean success, String message, T data, T errors) {
    this.success = success;
    this.message = message;
    this.data = data;
    this.errors = errors;
    this.timestamp = LocalDateTime.now();
  }
}
