package com.bandsyncapi.bandsyncapi.api.v1.services;

import java.util.List;

import com.bandsyncapi.bandsyncapi.api.v1.models.RolesModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.RolesPermissionsModel;

/**
 * Interface to defines the methods for the service
 */
public interface RolesPermissionsService {
  
  /**
   * Find all roles permissions by role
   * 
   * @param role - RolesModel object
   * @return - A list of RolesPermissionsModel
   */
  List<RolesPermissionsModel> findAllByRole(RolesModel role);
}
