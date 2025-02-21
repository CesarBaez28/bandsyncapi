package com.bandsyncapi.bandsyncapi.api.v1.dto.musicalbands;

import java.util.UUID;

import com.bandsyncapi.bandsyncapi.api.v1.models.UsersModel;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Represents the post request to save a new musical band
 */
public record MusicalBandsPostDto(
  UUID id, 

  @NotBlank(message = "El nombre es obligatorio.")
  @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres.")
  String name, 

  String logo, 
  String address, 
  String phone, 

  @NotBlank(message = "El email es obligatorio.")
  @Email(message = "El email debe ser válido.")
  String email, 
  
  Boolean status,

  UsersModel user
) {}
