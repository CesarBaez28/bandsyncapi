package com.bandsyncapi.bandsyncapi.api.v1.dto.roles;

import java.util.List;

import com.bandsyncapi.bandsyncapi.api.v1.dto.permissions.PermissionDto;

/**
 * This record represent a Role and its permissions
 */
public record RoleAndPermissionsDto(
  RolesDto role,
  List<PermissionDto> permissions
) {}
