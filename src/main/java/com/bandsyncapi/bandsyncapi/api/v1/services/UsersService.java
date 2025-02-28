package com.bandsyncapi.bandsyncapi.api.v1.services;

import java.util.List;
import java.util.UUID;

import com.bandsyncapi.bandsyncapi.api.v1.models.UsersModel;

/**
 * Interface that defines methods for UsersModel
 */
public interface UsersService {

  /**
   * Get all users that are part of a musical band
   * 
   * @param musicalBandId - Musical Band id
   * @return - A UsersModel List
   */
  List<UsersModel> getAllUsersByMusicalBandId(UUID musicalBandId); 
}
