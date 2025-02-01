package com.bandsyncapi.bandsyncapi.models;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/*
 * This class is a model for the musical_bands table in the database
 */
@Entity
@Data
@Table(name = "musical_bands")
public class MusicalBandsModel {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "name", nullable = false, unique = true, length = 100)
  private String name;

  @Column(name = "logo", nullable = false, length = 100, columnDefinition = "DEFAULT ''")
  private String logo;

  @Column(name = "address",nullable = false, length = 255, columnDefinition = "DEFAULT ''")
  private String address;

  @Column(name = "phone", nullable = false, length = 25, columnDefinition = "DEFAULT ''")
  private String phone;

  @Column(name = "email", nullable = false, unique = true, length = 50)
  private String email;

  @Column(name = "status", nullable = false, columnDefinition = "BIT DEFAULT 1")
  private Boolean status = true;
}
