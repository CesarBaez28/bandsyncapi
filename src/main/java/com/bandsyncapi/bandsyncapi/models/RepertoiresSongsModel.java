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
 * This class is the entity for the repertoires_songs table.
 */
@Entity
@Data
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

  @ManyToOne
  @MapsId("userId")
  @JoinColumn(name = "user_id")
  private UsersModel user;

  @Column(name = "status", nullable = false, columnDefinition = "BIT DEFAULT 1")
  private Boolean status;

  public RepertoiresSongsModel(RepertoiresModel repertoire, SongsModel song, UsersModel user) {
    this.repertoire = repertoire;
    this.song = song;
    this.user = user;
    this.id = new RepertoiresSongsKey(repertoire.getId(), song.getId(), user.getId());
  }
}
