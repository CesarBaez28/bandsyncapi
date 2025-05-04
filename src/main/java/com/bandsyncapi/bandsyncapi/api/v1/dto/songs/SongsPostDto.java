package com.bandsyncapi.bandsyncapi.api.v1.dto.songs;

import com.bandsyncapi.bandsyncapi.api.v1.models.ArtistsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalBandsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalGenresModel;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * This record represents the post request to save a song
 */
public record SongsPostDto(

  @Size(min = 3, message = "El nombre debe tener al menos 3 caracteres.")
  String name, 

  @NotNull
  MusicalBandsModel musicalBand,

  @NotNull(message = "Seleccione un artista.")
  ArtistsModel artist, 

  @NotNull(message = "Seleccione un género.")
  MusicalGenresModel genre,

  String tonality, 
  String link, 
  String sheetMusic,
  Boolean status
) {}
