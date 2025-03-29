package com.bandsyncapi.bandsyncapi.api.v1.dto.roles;

import java.util.List;

import com.bandsyncapi.bandsyncapi.api.v1.models.RolesPermissionsModel;

/**
 * This record represent the response request when a new Role is created
 */
public record RolesPermissionsDto(
  RolesDto role, 
  List<RolesPermissionsModel> permissions
) {}
