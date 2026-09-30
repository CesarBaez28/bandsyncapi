package com.bandsyncapi.bandsyncapi.api.v1.dto.musicalgenres;

import lombok.Builder;

/**
 * This record is a DTO for the MusicalGenreModel.
 */
@Builder 
public record MusicalGenreDto(Integer id, String name, Boolean status) {
  
}
