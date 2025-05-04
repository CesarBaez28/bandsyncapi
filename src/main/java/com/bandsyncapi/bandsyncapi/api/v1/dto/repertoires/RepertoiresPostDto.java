package com.bandsyncapi.bandsyncapi.api.v1.dto.repertoires;

import java.util.List;
import java.util.UUID;

import com.bandsyncapi.bandsyncapi.api.v1.models.SongsModel;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * This Dto represents the request body to save a repertoire
 */
public record RepertoiresPostDto(
  UUID musicalBand, 

  @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres.")  
  String name,

  String description,
  String link,
  Boolean status,

  @NotNull(message = "Debe agregar canciones al repertorio.")
  List<SongsModel> songs 
) {}
