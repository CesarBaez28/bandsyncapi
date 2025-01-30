package com.bandsyncapi.bandsyncapi.models;

import jakarta.persistence.*;
import lombok.Data;

/*
 * This class is a model for the artists table in the database.
 */
@Entity
@Data
@Table(name = "artists", indexes = {
  @Index(name = "artist_name_index", columnList = "name")
})
public class ArtistsModel {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "musical_band_id", nullable = false)
  private MusicalBandsModel musicalBand;

  @Column(name = "name", nullable = false, length = 100)
  private String name;

  @Column(name = "status", nullable = false, columnDefinition = "BIT DEFAULT 1")
  private Boolean status;
}
