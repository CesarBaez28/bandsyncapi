package com.bandsyncapi.bandsyncapi.api.v1.models;

import java.util.UUID;

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

/* 
 * This class is a model for the users table in the database
 */
@Entity
@Data
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsersModel {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "username", nullable = false, length = 100, unique = true)
  private String username;

  @Column(name = "password")
  private String password;

  @Column(name = "email", nullable = false, length = 100, unique = true)
  private String email;

  @Column(name = "firstname", nullable = false, length = 100)
  private String firstName;

  @Column(name = "lastname", nullable = false, length = 100)
  private String lastName;

  @Column(name = "phone", nullable = false, length = 20)
  private String phone;

  @Column(name = "photo", nullable = false, length = 100)
  private String photo;

  @Column(name = "status", nullable = false, columnDefinition = "BIT DEFAULT 1")
  private Boolean status;

  public UsersModel (UUID id) {
    this.id = id;
  }
}
