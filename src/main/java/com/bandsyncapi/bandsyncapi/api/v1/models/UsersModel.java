package com.bandsyncapi.bandsyncapi.api.v1.models;

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
import lombok.NoArgsConstructor;

/* 
 * This class is a model for the users table in the database
 */
@Entity
@Data
@Table(name = "users")
@NoArgsConstructor
public class UsersModel {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "role_id", nullable = false)
  private RolesModel role;

  @Column(name = "username", nullable = false, length = 100, unique = true)
  private String username;

  @Column(name = "password", nullable = false, length = 50)
  private String password;

  @Column(name = "email", nullable = false, length = 100, unique = true)
  private String email;

  @Column(name = "firstname", nullable = false, length = 100, columnDefinition = "DEFAULT ''")
  private String firstName;

  @Column(name = "lastname", nullable = false, length = 100, columnDefinition = "DEFAULT ''")
  private String lastName;

  @Column(name = "phone", nullable = false, length = 20, columnDefinition = "DEFAULT ''")
  private String phone;

  @Column(name = "photo", nullable = false, length = 100, columnDefinition = "DEFAULT ''")
  private String photo;

  @Column(name = "status", nullable = false, columnDefinition = "BIT DEFAULT 1")
  private Boolean status;

  public UsersModel (UUID id) {
    this.id = id;
  }
}
