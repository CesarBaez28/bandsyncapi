package com.bandsyncapi.bandsyncapi.api.v1.services;

import java.util.List;

import com.bandsyncapi.bandsyncapi.api.v1.dto.roles.RolesPermissionsDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.roles.RolesPostDto;
import com.bandsyncapi.bandsyncapi.api.v1.models.RolesModel;

/**
 * Interface that defines methods for RolesModel
 */
public interface RolesService {

  /**
   * Sava a new role
   * 
   * @param rolesModel - RolesModel object
   * @return - the new role
   */
  RolesModel save (RolesModel rolesModel);

  /**
   * Save a new roles with permissions
   * 
   * @param rolesPostDto - RolesPostDto Object
   * @return - RolesPermissionsDto Object
   */
  RolesPermissionsDto saveRoleAndPermissions (RolesPostDto rolesPostDto);

  /**
   * finds All roles
   * 
   * @return A RolesModel List
   */
  List<RolesModel> findAll ();
}
