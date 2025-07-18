package com.bandsyncapi.bandsyncapi.api.v1.dto.users;

import java.util.List;
import java.util.UUID;

import com.bandsyncapi.bandsyncapi.api.v1.dto.musicalbands.MusicalBandsDto;

/**
 * Dto for the UserSession
 * 
 * Contains user information and authentication token
 */
public record UserSessionDto(
  UUID id, 
  String username, 
  String accessToken, 
  List<MusicalBandsDto> musicalBands,
  String email, 
  String firstName, 
  String lastName, 
  String phone, 
  String photo, 
  Boolean status
) {}
