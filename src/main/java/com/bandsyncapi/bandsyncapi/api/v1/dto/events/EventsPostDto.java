package com.bandsyncapi.bandsyncapi.api.v1.dto.events;

import java.time.LocalDateTime;

import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalBandsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.RepertoiresModel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * This record represents the POST request for creating an event.
 */
public record EventsPostDto(
  MusicalBandsModel musicalBand, 

  @NotNull(message = "Seleccione un repertorio.")
  RepertoiresModel repertoire, 

  @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres.")
  String name, 

  @NotNull(message = "La fecha no puede estar vacía.")
  LocalDateTime date,

  String description, 

  @Size(min = 3, max = 100, message = "El lugar debe tener entre 3 y 100 caracteres.")
  String place, 

  @NotBlank(message = "La ubicación no puede estar vacía.")
  String location,

  Boolean status
) {}
