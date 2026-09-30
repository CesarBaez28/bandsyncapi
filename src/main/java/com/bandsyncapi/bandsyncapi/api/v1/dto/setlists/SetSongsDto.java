package com.bandsyncapi.bandsyncapi.api.v1.dto.setlists;

import java.util.UUID;

import com.bandsyncapi.bandsyncapi.api.v1.dto.songs.SongsDto;

import lombok.Builder;

/**
 * This Dto represents the information of a song in a set
 */
@Builder 
public record SetSongsDto(
  UUID id,
  SongsDto song, 
  Integer orderIndex, 
  String notes 
) {}
