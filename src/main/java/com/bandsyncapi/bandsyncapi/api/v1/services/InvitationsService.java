package com.bandsyncapi.bandsyncapi.api.v1.services;

import java.util.UUID;

import com.bandsyncapi.bandsyncapi.api.v1.models.InvitationsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.UsersModel;

import jakarta.mail.MessagingException;

/**
 * Invitations service
 */
public interface InvitationsService {
  
  /**
   * Send invitation to join a musical band
   * 
   * @param musicalBandId - musical band id 
   * @param email - email
   * @param invitedBy - user who send the invitation
   * @throws MessagingException
   */
  void sendInvitation(UUID musicalBandId, String email, UsersModel invitedBy) throws MessagingException;

  /**
   * Validate invitation token
   * 
   * @param token - Token
   * @return - The invitation info if the token is valid
   */
  InvitationsModel validateInvitationToken (String token);

  /**
   * Accept invitation
   * 
   * @param token - invitation token
   * @param userId - The user that accepted the invitation
   */
  void acceptInvitation (String token, UUID userId);

  /**
   * Find invitation by token
   * 
   * @param token - token
   * @return - The found invitation
   */
  InvitationsModel findByToken(String token);

  /**
   * Save invitation
   * 
   * @param invitation - invitation to save
   * @return - saved invitation
   */
  InvitationsModel save(InvitationsModel invitation);
} 
