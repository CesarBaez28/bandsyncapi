package com.bandsyncapi.bandsyncapi.api.v1.dto.roles;

import java.util.List;

import com.bandsyncapi.bandsyncapi.api.v1.dto.musicalbands.MusicalBandsDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.permissions.PermissionDto;

/**
 * This dto represents all user's roles in the different bands they belong to
 */
public record UserRolesAndPermissionsDto(
  MusicalBandsDto musicalBand,
  RolesDto role,
  List<PermissionDto> permissions
) {}
