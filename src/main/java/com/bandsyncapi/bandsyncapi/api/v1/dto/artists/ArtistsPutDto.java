package com.bandsyncapi.bandsyncapi.api.v1.dto.artists;

import jakarta.validation.constraints.Size;

/**
 * This record represent the update request to change artist name
 */
public record ArtistsPutDto(
  
  @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres.")
  String name
) {}
