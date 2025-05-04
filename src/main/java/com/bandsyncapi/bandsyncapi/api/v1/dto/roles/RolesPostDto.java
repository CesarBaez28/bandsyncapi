package com.bandsyncapi.bandsyncapi.api.v1.dto.roles;

import java.util.List;

import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalBandsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.PermissionsModel;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * This record represents the post request to save a new Role 
 */
public record RolesPostDto(
  
  @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
  String name, 

  @NotNull
  MusicalBandsModel musicalBand, 

  Boolean status,

  List<PermissionsModel> permissions
) {}
