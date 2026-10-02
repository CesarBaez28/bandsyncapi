package com.bandsyncapi.bandsyncapi.api.v1.dto.invitations;

import com.bandsyncapi.bandsyncapi.api.v1.models.UsersModel;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Represents the requesto to invite a person to a musical band
 */
public record InviteRequestDto(

  @Email(message = "Correo no válido")
  @NotBlank(message = "Debe proporcionar un correo")
  String email,

  UsersModel invitedBy
) {}
