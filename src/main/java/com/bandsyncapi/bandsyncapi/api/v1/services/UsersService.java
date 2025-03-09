package com.bandsyncapi.bandsyncapi.api.v1.services;

import java.util.List;
import java.util.UUID;

import com.bandsyncapi.bandsyncapi.api.v1.dto.users.UsersPutDto;
import com.bandsyncapi.bandsyncapi.api.v1.models.UsersModel;

/**
 * Interface that defines methods for UsersModel
 */
public interface UsersService {


  /**
   * Save a user
   * 
   * @param usersModel - A UsersModel object
   * @return - The new user
   */
  UsersModel save (UsersModel usersModel);

  /**
   * Get all users that are part of a musical band
   * 
   * @param musicalBandId - Musical Band id
   * @return - A UsersModel List
   */
  List<UsersModel> getAllUsersByMusicalBandId(UUID musicalBandId); 

  /**
   * Get user by Id
   * 
   * @param userId - user id
   * @return UsersModel object
   */
  UsersModel getById (UUID userId);

  /**
   * Update a user info
   * 
   * @param userId - user id
   * @param usersPutDto - A UsersPutDto with the data to be updated
   */
  void updateUser (UUID userId, UsersPutDto usersPutDto);
}
