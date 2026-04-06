package com.bandsyncapi.bandsyncapi.api.v1.dto.users;

import java.util.UUID;

import lombok.Builder;

/**
 * Dto for the UserSession
 * 
 * Contains user information and authentication token
 */
@Builder
public record UserSessionDto(
  UUID id, 
  String username, 
  String accessToken, 
  String email, 
  String firstName, 
  String lastName, 
  String phone, 
  String photo, 
  Boolean status
) {}
