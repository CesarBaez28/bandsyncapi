package com.bandsyncapi.bandsyncapi.api.v1.dto.users;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * This record represents the data transfer object for changing the user password
 */
public record ChangePasswordDto(

    @NotNull
    @NotBlank
    String username,

    @Pattern(
      regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()\\\\\\-_.+]).{8,}$", 
      message = "La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula, un número y un caracter especial") 
    @NotBlank(message = "La contraseña no puede estar vacía") 
    String oldPassword,

    @Pattern(
      regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()\\\\\\-_.+]).{8,}$", 
      message = "La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula, un número y un caracter especial") 
    @NotBlank(message = "La contraseña no puede estar vacía") 
    String newPassword) {
}
