package com.bandsyncapi.bandsyncapi.dto.musicalbands;

import java.util.UUID;

/**
 * This record represents the data transfer object for the musical_bands table in the database.
 */
public record MusicalBandsDto(UUID id, String name, String logo, String address, String phone, String email, Boolean status) {
  
}
