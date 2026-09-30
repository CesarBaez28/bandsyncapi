package com.bandsyncapi.bandsyncapi.api.v1.dto.setlists;

import java.util.List;

import lombok.Builder;

/**
 * This Dto represents the information of a set and its songs
 */
@Builder 
public record SetListSongsDto(
    SetDto set,
    List<SetSongsDto> songs
) {}
