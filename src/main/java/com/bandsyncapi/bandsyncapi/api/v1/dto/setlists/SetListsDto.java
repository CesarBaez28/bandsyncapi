package com.bandsyncapi.bandsyncapi.api.v1.dto.setlists;

import java.time.LocalDateTime;
import java.util.UUID;

import com.bandsyncapi.bandsyncapi.api.v1.models.RepertoiresModel;

/**
 * This Dto represents the data to be sent to the client
 */
public record SetListsDto(
    UUID id,
    RepertoiresModel repertoire,
    String name,
    String description,
    LocalDateTime createdAt,
    Boolean status
){}
