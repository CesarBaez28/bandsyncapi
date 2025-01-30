package com.bandsyncapi.bandsyncapi.models;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * This class is the many to many relationship between the roles and the permissions.
 */
@Entity
@Data
@Table(name = "roles_permissions")
public class RolesPermissionsModel {

  @EmbeddedId
  RolesPermissionsKey id;

  @ManyToOne
  @MapsId("roleId")
  @JoinColumn(name = "role_id")
  private RolesModel role;

  @ManyToOne
  @MapsId("permissionId")
  @JoinColumn(name = "permission_id")
  private PermissionsModel permission;

  @Column(name = "status", nullable = false, columnDefinition = "BIT DEFAULT 1")
  private Boolean status;

  public RolesPermissionsModel(RolesModel role, PermissionsModel permission) {
    this.role = role;
    this.permission = permission;
    this.id = new RolesPermissionsKey(role.getId(), permission.getId());
  }
}
