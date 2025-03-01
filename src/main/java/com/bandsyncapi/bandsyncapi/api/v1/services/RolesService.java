package com.bandsyncapi.bandsyncapi.api.v1.services;

import java.util.List;

import com.bandsyncapi.bandsyncapi.api.v1.models.RolesModel;

/**
 * Interface that defines methods for RolesModel
 */
public interface RolesService {

  /**
   * finds All roles
   * 
   * @return A RolesModel List
   */
  List<RolesModel> findAll ();
}
