package com.bandsyncapi.bandsyncapi.api.v1.models;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class is the entity for the set_list_songs table.
 */
@Entity
@Table(name = "set_list_songs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SetListSongsModel {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "set_id", nullable = false)
  private SetsModel set;

  @ManyToOne
  @JoinColumn(name = "song_id", nullable = false)
  private SongsModel song;

  @Column(name = "order_index", nullable = false)
  private Integer orderIndex;

  @Column(name = "notes", columnDefinition = "TEXT")
  private String notes;

  @Column(name = "status", nullable = false, columnDefinition = "BIT DEFAULT 1")
  @Builder.Default
  private Boolean status = true;
}
