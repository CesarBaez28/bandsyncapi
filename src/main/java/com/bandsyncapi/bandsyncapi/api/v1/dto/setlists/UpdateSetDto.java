package com.bandsyncapi.bandsyncapi.api.v1.dto.setlists;

import java.util.List;
import java.util.UUID;

/**
 * This dto represent part of the request when updating a set list.
 */
public record UpdateSetDto(
  UUID id,
  String name,
  Integer orderIndex,
  List<SetSongsRequestDto> songs
) {}
