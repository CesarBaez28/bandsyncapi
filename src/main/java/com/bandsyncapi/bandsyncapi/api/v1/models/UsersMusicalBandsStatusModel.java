package com.bandsyncapi.bandsyncapi.api.v1.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class is a model for the users_musical_bands_status table in the database.
 */
@Entity
@Table(name = "users_musical_bands_status")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsersMusicalBandsStatusModel {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "name", nullable = false, length = 100, unique = true)
  private String name;
}
