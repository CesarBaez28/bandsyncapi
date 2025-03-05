package com.bandsyncapi.bandsyncapi.api.v1.dto.events;

import java.time.LocalDateTime;
import java.util.UUID;

import com.bandsyncapi.bandsyncapi.api.v1.models.RepertoiresModel;

/**
 * This record represents the response for getting an event.
 */
public record EventsDto(
  UUID id,
  RepertoiresModel repertoire, 
  LocalDateTime date,
  String name,
  String description,
  String place,
  String location,
  Boolean status
) {}
