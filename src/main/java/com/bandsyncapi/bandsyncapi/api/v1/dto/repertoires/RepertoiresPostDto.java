package com.bandsyncapi.bandsyncapi.api.v1.dto.repertoires;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * This Dto represents the request body to save a repertoire
 */
public record RepertoiresPostDto(
  UUID musicalBand, 

  @NotBlank
  @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres.")  
  String name,

  String description,
  String link,
  Boolean status
) {}
