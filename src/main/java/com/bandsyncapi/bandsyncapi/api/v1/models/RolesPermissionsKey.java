package com.bandsyncapi.bandsyncapi.api.v1.models;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class is the key for the RolesPermissions class.
 */
@Embeddable
@AllArgsConstructor @NoArgsConstructor
@Data
public class RolesPermissionsKey implements Serializable {

  private static final long serialVersionUID = 1L;

  @Column(name = "role_id")
  private Integer roleId;

  @Column(name = "permission_id")
  private Integer permissionId;
}
