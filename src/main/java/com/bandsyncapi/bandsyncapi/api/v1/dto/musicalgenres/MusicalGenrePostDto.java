package com.bandsyncapi.bandsyncapi.api.v1.dto.musicalgenres;

import java.util.UUID;

/*
 * This record represent the body request for creating a new musical genre 
 */
public record MusicalGenrePostDto(String name, UUID musicalBandId) {
  
}
