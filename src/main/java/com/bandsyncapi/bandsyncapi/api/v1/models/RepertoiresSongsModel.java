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
 * This class is the entity for the repertoires_songs table.
 */
@Entity
@Data
@NoArgsConstructor
@Table(name = "repertoires_songs")
public class RepertoiresSongsModel {

  @EmbeddedId
  private RepertoiresSongsKey id;

  @ManyToOne
  @MapsId("repertoireId")
  @JoinColumn(name = "repertoire_id")
  private RepertoiresModel repertoire;

  @ManyToOne
  @MapsId("songId")
  @JoinColumn(name = "song_id")
  private SongsModel song;

  @Column(name = "status", nullable = false, columnDefinition = "BIT DEFAULT 1")
  private Boolean status;

  public RepertoiresSongsModel(RepertoiresModel repertoire, SongsModel song, Boolean status) {
    this.repertoire = repertoire;
    this.song = song;
    this.status = status;
    this.id = new RepertoiresSongsKey(repertoire.getId(), song.getId());
  }
}
