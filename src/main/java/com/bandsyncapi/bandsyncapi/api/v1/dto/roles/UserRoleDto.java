package com.bandsyncapi.bandsyncapi.api.v1.dto.roles;

import com.bandsyncapi.bandsyncapi.api.v1.dto.musicalbands.MusicalBandsDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.users.UsersDto;

/**
 * This dto represents an user and his role in a musical band
 */
public record UserRoleDto(
  RolesDto role,
  MusicalBandsDto musicalBand,
  UsersDto user
) {}
