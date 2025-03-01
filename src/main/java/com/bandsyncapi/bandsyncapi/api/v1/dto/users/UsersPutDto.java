package com.bandsyncapi.bandsyncapi.api.v1.dto.users;

import com.bandsyncapi.bandsyncapi.api.v1.models.RolesModel;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Dto the represents the put request to update a user
 */
public record UsersPutDto(
  @NotNull
  RolesModel role,

  @NotBlank(message = "El campo username esta vacío")
  @Size(min = 3, max = 100, message = "El nombre de usuario debe tener entre 3 y 100 caracteres.")  
  String username,

  @NotBlank(message = "El campo correo electrónico está vacío")
  @Email(message = "Correo electrónico no válido")
  String email,

  String firstName,
  String lastName,
  String phone,
  String photo, 
  Boolean status
) {} 
