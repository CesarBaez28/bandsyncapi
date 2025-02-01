package com.bandsyncapi.bandsyncapi.models;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

/*
 * This class is a model for the events table in the database.
 */
@Entity
@Data
@Table(name = "events")
public class EventsModel {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "musical_band_id")
  private MusicalBandsModel musicalBand;

  @ManyToOne
  @JoinColumn(name = "repertoire_id")
  private RepertoiresModel repertoire;

  @Column(name = "date", nullable = false)
  private LocalDate date;

  @Column(name = "name", nullable = false, length = 100)
  private String name;

  @Column(name = "description", nullable = false, columnDefinition = "TEXT")
  private String description;

  @Column(name = "place", nullable = false, length = 255)
  private String place;

  @Column(name = "location", nullable = false, length = 255)
  private String location;

  @Column(name = "status", nullable = false, columnDefinition = "BIT DEFAULT 1")
  private Boolean status;
}
