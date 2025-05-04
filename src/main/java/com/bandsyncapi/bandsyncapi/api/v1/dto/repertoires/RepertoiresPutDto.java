package com.bandsyncapi.bandsyncapi.api.v1.dto.repertoires;

import jakarta.validation.constraints.Size;

/**
 * This Dto represents the update request for RepertoiresModel
 */
public record RepertoiresPutDto(
   
  @Size(min = 3, max = 100, message = "El nombre de tener de 3 a 100 caracteres")
  String name, 
  String description, 
  String link, 
  Boolean status
) {}
