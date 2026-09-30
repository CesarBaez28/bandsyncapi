package com.bandsyncapi.bandsyncapi.api.v1.dto.setlists;

import java.util.List;

/**
 * This dto represent part of the request when creating a new set list.
 */
public record CreateSetDto(
    String name,
    Integer orderIndex,
    List<SetSongsRequestDto> songs
) {}
