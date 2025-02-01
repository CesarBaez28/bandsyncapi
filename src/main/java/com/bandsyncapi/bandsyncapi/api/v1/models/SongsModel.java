package com.bandsyncapi.bandsyncapi.api.v1.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

/*
 * This class is a model for the songs table in the database.
 */
@Entity
@Data
@Table(name = "songs", indexes = {
  @Index(name = "song_name_index", columnList = "name")
})
public class SongsModel {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "artist_id", nullable = false)
  private ArtistsModel artist;

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "genre_id", nullable = false)
  private MusicalGenresModel genre;

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "musical_band_id", nullable = false)
  private MusicalBandsModel musicalBand;

  @Column(name = "tonality", nullable = false, length = 25, columnDefinition = "DEFAULT ''")
  private String tonality;

  @Column(name = "link", nullable = false, length = 255, columnDefinition = "DEFAULT ''")
  private String link;

  @Column(name = "sheet_music", nullable = false, length = 255, columnDefinition = "DEFAULT ''")
  private String sheetMusic;

  @Column(name = "description", nullable = false, length = 255, columnDefinition = "TEXT")
  private String description;

  @Column(name = "name", nullable = false, length = 100)
  private String name;

  @Column(name = "status", nullable = false, columnDefinition = "BIT DEFAULT 1")
  private Boolean status;
}
