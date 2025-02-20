package com.bandsyncapi.bandsyncapi.api.v1.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * This class is a model for the artists table in the database.
 */
@Entity
@Data
@NoArgsConstructor
@Table(
  name = "artists", 
  uniqueConstraints = {
    @UniqueConstraint(columnNames = {"musical_band_id", "name"})
  },
  indexes = {
    @Index(name = "artist_name_index", columnList = "name")
  }
)
public class ArtistsModel {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne
  @JoinColumn(name = "musical_band_id", nullable = false)
  private MusicalBandsModel musicalBand;

  @Column(name = "name", nullable = false, length = 100)
  private String name;

  @Column(name = "status", nullable = false, columnDefinition = "BIT DEFAULT 1")
  private Boolean status;

  public ArtistsModel (Integer id) {
    this.id = id;
  }
}
