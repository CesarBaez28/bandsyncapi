package com.bandsyncapi.bandsyncapi.api.v1.dto.setlists;

import java.util.List;

import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalBandsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.RepertoiresModel;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * This dto represent the request to update a set list
 */
public record UpdateSetListDto(

    @NotNull(message = "Debe escribir un nombre para el set list")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres") 
    String name,

    @NotNull(message = "Seleccione un repertorio")
    RepertoiresModel repertoire,

    @NotNull(message = "Banda musical es requerida")
    MusicalBandsModel musicalBand,

    String description,

    List<UpdateSetDto> sets
) {}
