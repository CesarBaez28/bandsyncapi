package com.bandsyncapi.bandsyncapi.api.v1.dto.songs;

import com.bandsyncapi.bandsyncapi.api.v1.models.ArtistsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalGenresModel;

/**
 * This record represents the song data to be share
 */
public record SongsDto(Integer id, String name, ArtistsModel artist, MusicalGenresModel genre, String tonality,
    String link, String sheetMusic) {
}
