package com.bandsyncapi.bandsyncapi.api.v1.models;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class is used to store set lists for the users.
 * A set list is a collection of sets that are used to organize the songs based
 * on a repertoire.
 */
@Entity
@Table(name = "set_lists")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SetListsModel {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "repertoire_id", nullable = false)
  private RepertoiresModel repertoire;

  @ManyToOne
  @JoinColumn(name = "musical_band_id", nullable = false)
  private MusicalBandsModel musicalBand;

  @Column(name = "name", nullable = false, length = 100)
  private String name;

  @Column(name = "description", columnDefinition = "TEXT")
  private String description;

  @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
  private LocalDateTime createdAt;

  @Column(name = "status", nullable = false, columnDefinition = "BIT DEFAULT 1")
  @Builder.Default
  private boolean status = true;

  public SetListsModel(UUID id) {
    this.id = id;
  }
}