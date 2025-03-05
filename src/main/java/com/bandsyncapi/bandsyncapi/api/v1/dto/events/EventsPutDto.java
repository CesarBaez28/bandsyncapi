package com.bandsyncapi.bandsyncapi.api.v1.dto.events;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * This record represents the PUT request for updating an event.
 */
public record EventsPutDto(
  @NotNull(message = "Seleccione un repertorio.")
  UUID repertoireId, 

  @NotNull(message = "La fecha no puede estar vacía.")
  LocalDateTime date, 

  @NotBlank(message = "El nombre no puede estar vacío.")
  @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres.")
  String name, 

  String description,

  @NotBlank(message = "El lugar no puede estar vacío.")
  @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres.")
  String place, 

  @NotBlank(message = "La ubicación no puede estar vacía.")
  String location, 

  Boolean status
) {}
