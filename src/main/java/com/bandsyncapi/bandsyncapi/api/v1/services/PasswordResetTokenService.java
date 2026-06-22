package com.bandsyncapi.bandsyncapi.api.v1.services;

import java.util.UUID;

import jakarta.mail.MessagingException;

/**
 * Interface that defines methods for PasswordResetTokenModel
 */
public interface PasswordResetTokenService {

  /**
   * Generate a password reset token for a user and send it to the user's email
   * 
   * @param email - The email of the user requesting a password reset
   * @throws MessagingException if there is an error sending the email
   */
  void forgotPassword(String email) throws MessagingException;

  /**
   * Reset the user's password using the provided token and new password
   * 
   * @param token       - The password reset token
   * @param newPassword - The new password to set for the user
   */
  void resetPassword(UUID token, String newPassword);

  /**
   * Deletes all token by user id
   * 
   * @param userId - user id
   */
  void deleteByUserId(UUID userId);
}
