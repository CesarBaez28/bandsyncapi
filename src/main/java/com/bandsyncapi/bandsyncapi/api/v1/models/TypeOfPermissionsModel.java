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
 * In this class are defined all the types of permissions that the system can have.
 */
@Entity
@Table(name = "types_permissions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TypeOfPermissionsModel {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "name", nullable = false, unique = true)
  private String name;

  @Column(name = "status", nullable = false, columnDefinition = "BIT DEFAULT 1")
  private Boolean status;

  /**
   * Constructor with id
   * 
   * @param id - TypeOfPermissionsModel id
   */
  public TypeOfPermissionsModel(Integer id) {
    this.id = id;
  }
}
