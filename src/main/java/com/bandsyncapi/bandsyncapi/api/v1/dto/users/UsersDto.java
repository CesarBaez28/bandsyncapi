package com.bandsyncapi.bandsyncapi.api.v1.dto.users;

import java.util.UUID;

/**
 * Dto for the UsersModel 
 */
public record UsersDto(
  UUID id, 
  String username,
  String email, 
  String firstName, 
  String lastName, 
  String phone, 
  String photo, 
  Boolean status
) {}
