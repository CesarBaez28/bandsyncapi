package com.bandsyncapi.bandsyncapi.api.v1.dto.artists;

import java.util.UUID;

import jakarta.validation.constraints.Size;

/**
 * This record represents the post request to save a new Artist.
 */
public record ArtistsPostDto(
  @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres.")  
  String name,
  
  UUID musicalBandId
) {}
