package com.bandsyncapi.bandsyncapi.api.v1.services;

import java.util.List;
import java.util.UUID;

import com.bandsyncapi.bandsyncapi.api.v1.dto.roles.RoleAndPermissionsDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.roles.UserRolesAndPermissionsDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.roles.UserRoleDto;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalBandsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.RolesModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.UsersModel;
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

  /**
   * Finds users with a specific role
   * 
   * @param roleId - role id
   * @return - List of users with that role id
   */
  List<UsersRolesModel> findByRoleId(Integer roleId);

  /**
   * find the role of ther user in a musical band
   * 
   * @param userId
   * @param musicalBandId
   * @return the user en his role in the musical band
   */
  RoleAndPermissionsDto findByUserIdAndMusicalBandId(UUID userId, UUID musicalBandId);
  
  /**
   * Find all of a user's roles in the different bands they belong to 
   * 
   * @param user - UsersModel
   * @return all user roles
   */
  List<UserRolesAndPermissionsDto> findByUser (UsersModel user);

  /**
   * find all user'roles in a musical band
   * 
   * @param musicalBand - musical band
   * @return - List of users and his roles
   */
  List<UserRoleDto> findByMusicalBand(MusicalBandsModel musicalBand);

  /**
   * Update user role
   * 
   * @param role - roles model
   * @param userId -  user id
   * @param musicalBandId - musicalBand id
   */
  void updateUserRole (RolesModel role, UUID userId, UUID musicalBandId);
}
