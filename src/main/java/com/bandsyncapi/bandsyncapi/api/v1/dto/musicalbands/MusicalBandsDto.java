package com.bandsyncapi.bandsyncapi.api.v1.dto.musicalbands;

import java.util.UUID;


/**
 * This record represents the data transfer object for the MusicalBandsModel
 */
public record MusicalBandsDto(
  UUID id, 

  String name, 

  String hyphenatedName,

  String logo, 
  String address, 
  String phone, 

  String email, 
  
  Boolean status
) {}
