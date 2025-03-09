package com.bandsyncapi.bandsyncapi.api.v1.dto.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * This record represents the data transfer object for the user registration
 */
public record UserRegisterPostDto(

  @NotBlank(message = "El nombre de usuario no puede estar vacío")
  @Size(min = 3, message = "El nombre de usuario debe tener al menos 3 caracteres")
  String username,

  @Email(message = "El correo electrónico no es válido")
  String email,

  @Pattern(
    regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()\\\\\\-_.+]).{8,}$",
    message = "La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula, un número y un caracter especial"
  )
  @NotBlank(message = "La contraseña de usuario no puede estar vacío")
  String password,

  @NotBlank(message = "Repita la contraseña de nuevo")
  String repeatedPassword
) {}
