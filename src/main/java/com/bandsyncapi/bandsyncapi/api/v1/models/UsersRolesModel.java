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

/**
 * This class is a model for the users_roles table in the database.
 */
@Entity
@Data
@Table(name = "users_roles")
@NoArgsConstructor
public class UsersRolesModel {

  @EmbeddedId
  private UsersRolesKey id;

  @ManyToOne
  @MapsId("roleId")
  @JoinColumn(name = "role_id")
  private RolesModel role;

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

  public UsersRolesModel(RolesModel role, MusicalBandsModel musicalBand, UsersModel user, Boolean status) {
    this.role = role;
    this.musicalBand = musicalBand;
    this.user = user;
    this.status = status;
    this.id = new UsersRolesKey(role.getId(), user.getId(), musicalBand.getId());
  }
}
