package com.bandsyncapi.bandsyncapi.api.v1.dto.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

/**
 * Dto the represents the put request to update a user
 */
public record UsersPutDto(
  @Size(min = 3, max = 100, message = "El nombre de usuario debe tener entre 3 y 100 caracteres.")  
  String username,

  @Email(message = "Correo electrónico no válido")
  String email,
  
  String firstName,
  String lastName,
  String phone,
  String photo, 
  Boolean status
) {} 
