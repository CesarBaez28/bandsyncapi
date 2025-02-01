package com.bandsyncapi.bandsyncapi.api.v1.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

/*
 * This class is a model for the musical_genres table in the database.
 */
@Entity
@Data
@Table(name = "musical_genres", indexes = {
  @Index(name = "musical_genre_name_index", columnList = "name")
})
public class MusicalGenresModel {

  @Id
  @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne
  @JoinColumn(name = "musical_band_id", nullable = false)
  private MusicalBandsModel musicalBand;

  @Column(name = "name", nullable = false, length = 100)
  private String name;

  @Column(name = "status", nullable = false, columnDefinition = "BIT DEFAULT 1")
  private Boolean status;
}
