package com.bandsyncapi.bandsyncapi.api.v1.dto.songs;

import com.bandsyncapi.bandsyncapi.api.v1.dto.artists.ArtistsDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.musicalgenres.MusicalGenreDto;

/**
 * This record represents the song data to be share
 */
public record SongsDto(Integer id, String name, ArtistsDto artist, MusicalGenreDto genre, String tonality,
    String link, String sheetMusic) {
}
