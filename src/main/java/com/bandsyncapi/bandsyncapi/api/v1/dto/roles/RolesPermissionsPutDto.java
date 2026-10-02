package com.bandsyncapi.bandsyncapi.api.v1.dto.roles;

import java.util.List;

import com.bandsyncapi.bandsyncapi.api.v1.models.PermissionsModel;

import jakarta.validation.constraints.Size;

/**
 * This record represents the put request to update a Role and its permissions
 */
public record RolesPermissionsPutDto(
  Integer roleId,  

  @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
  String newName,

  List<PermissionsModel> permissions 
) {}
