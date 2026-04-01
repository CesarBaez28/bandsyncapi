package com.bandsyncapi.bandsyncapi.api.v1.services;

import java.time.LocalDateTime;

import jakarta.mail.MessagingException;

/**
 * Email service interface
 */
public interface EmailService {

  /**
   * Send an email invitation to join a musical band
   * 
   * @param to             - email to send the invitation
   * @param bandName       - musical band name
   * @param invitedBy      - the person who send the invitation
   * @param invitationLink - invitation link to join a musical band
   * @param expirationDate - expiration date of invitation
   * @throws MessagingException
   */
  void sendInvitationEmail(String to, String bandName, String invitedBy, String invitationLink,
      LocalDateTime expirationDate) throws MessagingException;

  /**
   * Send an email to reset the user's password
   * 
   * @param to        - email to send the reset password email
   * @param resetLink - reset password link
   * @throws MessagingException
   */
  void sendResetPasswordEmail(String to, String resetLink) throws MessagingException;
}
