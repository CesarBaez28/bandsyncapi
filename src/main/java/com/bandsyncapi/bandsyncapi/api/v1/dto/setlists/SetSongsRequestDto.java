package com.bandsyncapi.bandsyncapi.api.v1.dto.setlists;

import java.util.UUID;

/**
 * This Dto represents the information of a song in a set
 * for the request when creating or updating.
 */
public record SetSongsRequestDto (
  UUID setSongId,
  Integer songId,
  Integer orderIndex,
  String notes
) {}
