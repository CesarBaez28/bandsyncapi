package com.bandsyncapi.bandsyncapi.api.v1.dto.users;

import java.util.UUID;

import com.bandsyncapi.bandsyncapi.api.v1.models.RolesModel;

/**
 * Dto for the UsersModel 
 */
public record UsersDto(
  UUID id, 
  RolesModel role,
  String username,
  String email, 
  String firstName, 
  String lastName, 
  String phone, 
  String photo, 
  Boolean status
) {}
