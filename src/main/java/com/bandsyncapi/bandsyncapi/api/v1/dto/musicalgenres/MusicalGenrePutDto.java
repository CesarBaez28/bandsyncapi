package com.bandsyncapi.bandsyncapi.api.v1.dto.musicalgenres;

import jakarta.validation.constraints.Size;

/**
 * This record represents a Dto to update the musical genre name
 */
public record MusicalGenrePutDto(
  @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres.")
  String name
) {}
