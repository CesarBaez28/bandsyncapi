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

@Entity
@Data
@Table(name = "absences")
public class AbsencesModel {
  
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "musical_band_id")
  private MusicalBandsModel musicalBand;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private UsersModel user;

  @Column(name = "date_from", nullable = false)
  private LocalDate dateFrom;

  @Column(name = "date_to", nullable = false)
  private LocalDate dateTo;

  @Column(name = "description", nullable = false, columnDefinition = "TEXT")
  private String description;

  @Column(name = "status", nullable = false, columnDefinition = "BIT DEFAULT 1")
  private Boolean status;
}
