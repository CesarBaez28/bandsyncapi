package com.bandsyncapi.bandsyncapi.api.v1.dto.artists;

import lombok.Builder;

/**
 * This record is a DTO for the ArtistsModel.
 */
@Builder 
public record ArtistsDto(Integer id, String name, Boolean status) {}
