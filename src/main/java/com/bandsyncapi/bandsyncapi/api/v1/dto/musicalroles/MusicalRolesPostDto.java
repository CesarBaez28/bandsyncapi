package com.bandsyncapi.bandsyncapi.api.v1.dto.musicalroles;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/*
 * This record represent the body request for creating a new musical
 */
public record MusicalRolesPostDto(
  @NotBlank(message = "El campo nombre es obligatorio.")
  @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres.")
  String name, 
  
  UUID musicalBandId
) {}

  
