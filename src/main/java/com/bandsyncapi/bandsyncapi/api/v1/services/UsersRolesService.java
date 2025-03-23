package com.bandsyncapi.bandsyncapi.api.v1.services;

import com.bandsyncapi.bandsyncapi.api.v1.models.UsersRolesModel;

/**
 * This interface defines the methods that the UsersRolesServiceImpl class should implement.
 */
public interface UsersRolesService {

  /**
   * Save a new user role for a musical band.
   * 
   * @param usersRolesModel - UsersRolesModel
   * @return - the saved UsersRolesModel
   */
  UsersRolesModel save (UsersRolesModel usersRolesModel);
}
