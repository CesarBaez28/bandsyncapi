package com.bandsyncapi.bandsyncapi.api.v1.models;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/*
 * This class is the key for the RepertoiresSongs class.
 */
@Embeddable
@AllArgsConstructor @NoArgsConstructor
public class RepertoiresSongsKey implements Serializable {
  
  private static final long serialVersionUID = 1L;

  @Column(name = "repertoire_id")
  private UUID repertoireId;

  @Column(name = "song_id")
  private Integer songId;
}
