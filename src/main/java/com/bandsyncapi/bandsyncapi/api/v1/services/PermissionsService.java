package com.bandsyncapi.bandsyncapi.api.v1.services;

import java.util.List;

import com.bandsyncapi.bandsyncapi.api.v1.models.PermissionsModel;

/**
 * Interface to defines the methods for the service
 */
public interface PermissionsService {
  
  /**
   * Find all permissions
   * 
   * @return - A list of PermissionsModel
   */
  List<PermissionsModel> findAll ();

  /**
   * finds a permission by id
   * 
   * @param id - permission id
   * @return - PermissionsModel object
   */
  PermissionsModel findById (Integer id);
}
