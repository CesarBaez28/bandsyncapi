package com.bandsyncapi.bandsyncapi.api.v1.dto.setlists;

import java.util.List;

import lombok.Builder;

/**
 * This Dto represents the information of a setlist and its sets
 */
@Builder 
public record SetListDetailsDto(
    SetListsDto setList,
    List<SetListSongsDto> sets
) {}
