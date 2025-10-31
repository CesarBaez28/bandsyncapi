package com.bandsyncapi.bandsyncapi.api.v1.services;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.bandsyncapi.bandsyncapi.api.v1.dto.roles.RoleAndPermissionsDto;
import com.bandsyncapi.bandsyncapi.api.v1.models.RolesModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.RolesPermissionsModel;

/**
 * Interface to defines the methods for the service
 */
public interface RolesPermissionsService {

  /**
   * Save a RolesPermissionsModel
   * 
   * @param rolesPermissionsModel - A RolesPermissionsModel object
   * @return - The new RolesPermissionsModel
   */
  RolesPermissionsModel save (RolesPermissionsModel rolesPermissionsModel);

  /**
   * Find all roles permissions by role
   * 
   * @param role - RolesModel object
   * @return - A list of RolesPermissionsModel
   */
  List<RolesPermissionsModel> findAllByRole(RolesModel role);

  /**
   * Find all roles permissions by musical band id
   * 
   * @param musicalBandId - musical band id
   * @return - A list of RoleAndPermissionsDto
   */
  List<RoleAndPermissionsDto> findByMusicalBandId (UUID musicalBandId);

  /**
   * Save a List of RolesPermissionsModel
   * 
   * @param rolesPermissionsModel - List of RolesPermissionsModel to save
   * @return - A list of RolesPermissionsModel
   */
  List<RolesPermissionsModel> saveAll(List<RolesPermissionsModel> rolesPermissionsModel);

  /**
   * Delete all permissions of a role
   * 
   * @param id - Role id
   * @param permissionsToDelete - List of permission ids to delete
   */
  void deleteByRoleIdAndPermissionIds(Integer id, Set<Integer> permissionsToDelete);

  /**
   * Delete by role id
   * 
   * @param roleId - role id
   */
  void deleteByRoleId(Integer roleId);
}
