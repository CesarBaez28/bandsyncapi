package com.bandsyncapi.bandsyncapi.api.v1.dto.invitations;

import lombok.Builder;

/**
 * Represents the response when accepting an invitation
 */
@Builder
public record AcceptInvitationDto(
  boolean userExist,

  String email,

  String bandName
) {}
