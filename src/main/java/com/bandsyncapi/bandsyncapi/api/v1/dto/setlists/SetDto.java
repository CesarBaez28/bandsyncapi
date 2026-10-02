package com.bandsyncapi.bandsyncapi.api.v1.dto.setlists;

import java.util.UUID;

import lombok.Builder;

/**
 * Set Dto to transfer data
 */
@Builder 
public record SetDto(
    UUID id,
    SetListsDto setList,
    String name,
    Integer orderIndex,
    Boolean status
) {}
