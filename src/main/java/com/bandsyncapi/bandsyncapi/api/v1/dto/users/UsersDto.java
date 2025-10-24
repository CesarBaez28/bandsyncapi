package com.bandsyncapi.bandsyncapi.api.v1.dto.users;

import java.util.UUID;

import com.bandsyncapi.bandsyncapi.api.v1.models.UsersStatusModel;

/**
 * Dto for the UsersModel 
 */
public record UsersDto(
  UUID id, 
  UsersStatusModel userStatus,
  String username,
  String email, 
  String firstName, 
  String lastName, 
  String phone, 
  String photo, 
  Boolean status
) {}
