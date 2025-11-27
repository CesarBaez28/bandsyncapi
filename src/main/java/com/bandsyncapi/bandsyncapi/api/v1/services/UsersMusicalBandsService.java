package com.bandsyncapi.bandsyncapi.api.v1.services;

import java.util.List;
import java.util.UUID;

import com.bandsyncapi.bandsyncapi.api.v1.dto.musicalbands.MusicalBandsDto;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalBandsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.UsersModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.UsersMusicalBandsModel;

/**
 * Defines methods for UsersMusicalBandsModel
 */
public interface UsersMusicalBandsService {

  /**
   * Save a UsersMusicalBandsModel
   * 
   * @param user - User
   * @param musicalBand - Musical Band
   * @return - UsersMusicalBandsModel Object
   */
  UsersMusicalBandsModel save (UsersModel user, MusicalBandsModel musicalBand);

  /**
   * Find all the bands a user is a part of
   * 
   * @param usersModel - UsersModel object
   * @return A list of UsersMusicalBandsModel
   */
  List<MusicalBandsDto> findByUser (UsersModel usersModel);

  /**
   * Delete a user from a musical band
   * 
   * @param userId - UUID of the user
   * @param musicalBandId - UUID of the musical band
   */
  void deleteByUserIdAndMusicalBandId(UUID userId, UUID musicalBandId);
}
