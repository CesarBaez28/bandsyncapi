package com.bandsyncapi.bandsyncapi.api.v1.dto.musicalbands;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

/**
 * repersents the put request for musical bands
 */
@Builder
public record MusicalBandPutDto(
  @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres.") 
  String name,

  String address,
  String phone,

  @NotBlank(message = "El email es obligatorio.") @Email(message = "El email debe ser válido.")
  String email,

  String logo
) {}
