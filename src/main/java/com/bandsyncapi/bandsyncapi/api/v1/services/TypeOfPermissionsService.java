package com.bandsyncapi.bandsyncapi.api.v1.services;

import java.util.List;

import com.bandsyncapi.bandsyncapi.api.v1.models.TypeOfPermissionsModel;

/**
 * Service interface for TypeOfPermissionsModel
 */
public interface TypeOfPermissionsService {

  /**
   * Find all types of permissions
   * 
   * @return - A list of TypeOfPermissionsModel
   */
  List<TypeOfPermissionsModel> findAll();
}
