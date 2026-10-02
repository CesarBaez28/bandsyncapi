package com.bandsyncapi.bandsyncapi.api.v1.models;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * This class is a model for the musical_roles_users table in the database.
 */
@Entity
@Data
@NoArgsConstructor
@Table(name = "musical_roles_users")
public class MusicalRolesUsersModel {

  @EmbeddedId
  private MusicalRolesUsersKey id;

  @ManyToOne
  @MapsId("musicalRoleId")
  @JoinColumn(name = "musical_role_id")
  private MusicalRolesModel musicalRole;

  @ManyToOne
  @MapsId("musicalBandId")
  @JoinColumn(name = "musical_band_id")
  private MusicalBandsModel musicalBand;

  @ManyToOne
  @MapsId("userId")
  @JoinColumn(name = "user_id")
  private UsersModel user;

  @Column(name = "status", nullable = false, columnDefinition = "BIT DEFAULT 1")
  private Boolean status;

  public MusicalRolesUsersModel(MusicalRolesModel musicalRole, MusicalBandsModel musicalBand, UsersModel user, Boolean status) {
    this.musicalRole = musicalRole;
    this.musicalBand = musicalBand;
    this.user = user;
    this.status = status;
    this.id = new MusicalRolesUsersKey(musicalRole.getId(), user.getId(), musicalBand.getId());
  }
}
