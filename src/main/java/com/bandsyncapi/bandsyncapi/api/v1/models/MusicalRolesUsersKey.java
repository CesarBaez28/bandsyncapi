package com.bandsyncapi.bandsyncapi.api.v1.models;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * This class is the key for the MusicalRolesUsers class.
 */
@Embeddable
@AllArgsConstructor @NoArgsConstructor
@Data
public class MusicalRolesUsersKey implements Serializable {

  private static final long serialVersionUID = 1L;

  @Column(name = "musical_role_id")
  private Integer musicalRoleId;

  @Column(name = "user_id")
  private UUID userId;

  @Column(name = "musical_band_id")
  private UUID musicalBandId;
}
