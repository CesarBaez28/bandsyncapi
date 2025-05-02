package com.bandsyncapi.bandsyncapi.api.v1.dto.musicalroles;

import jakarta.validation.constraints.Size;

/**
 * This record represents a Put request to for the MusicalRolesModel
 */
public record MusicalRolesPutDto(
  @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres.")
  String name
) {}
