package com.bandsyncapi.bandsyncapi.api.v1.models;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/*
 * This class is the key for the UsersMusicalBands class.
 */
@Embeddable
@AllArgsConstructor @NoArgsConstructor
public class UsersMusicalBandsKey implements Serializable {

  private static final long serialVersionUID = 1L;

  @Column(name = "user_id")
  private UUID userId;

  @Column(name = "musical_band_id")
  private UUID musicalBandId;
}
