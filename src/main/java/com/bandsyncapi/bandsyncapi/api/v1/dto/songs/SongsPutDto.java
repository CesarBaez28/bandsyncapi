package com.bandsyncapi.bandsyncapi.api.v1.dto.songs;

import com.bandsyncapi.bandsyncapi.api.v1.models.ArtistsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalGenresModel;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * This record represents the update request to change song info
 */
public record SongsPutDto(

  @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
  String name, 

  @NotNull(message = "Seleccione un artista")
  ArtistsModel artist, 

  @NotNull(message = "Seleccione un género")
  MusicalGenresModel genre, 

  String tonality, 
  String link, 
  String sheetMusic
) {}
