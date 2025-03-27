package com.bandsyncapi.bandsyncapi.api.v1.models;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class is the many to many relationship between the roles and the permissions.
 */
@Entity
@Data
@Table(name = "roles_permissions")
@NoArgsConstructor
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

  public RolesPermissionsModel(RolesModel role, PermissionsModel permission, Boolean status) {
    this.role = role;
    this.permission = permission;
    this.status = status;
    this.id = new RolesPermissionsKey(role.getId(), permission.getId());
  }
}
