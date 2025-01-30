package com.bandsyncapi.bandsyncapi.models;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Data;

/*
 * This class is a model for the users_musical_bands table in the database.
 */
@Entity
@Data
@Table(name = "users_musical_bands")  
public class UsersMusicalBandsModel {
  
  @EmbeddedId
  private UsersMusicalBandsKey id;
  
  @ManyToOne
  @MapsId("userId")
  @JoinColumn(name = "user_id")
  private UsersModel user;
  
  @ManyToOne
  @MapsId("musicalBandId")
  @JoinColumn(name = "musical_band_id")
  private MusicalBandsModel musicalBand;

  @Column(name = "status", nullable = false, columnDefinition = "BIT DEFAULT 1")
  private Boolean status;
  
  public UsersMusicalBandsModel(UsersModel user, MusicalBandsModel musicalBand) {
    this.user = user;
    this.musicalBand = musicalBand;
    this.id = new UsersMusicalBandsKey(user.getId(), musicalBand.getId());
  }
}
