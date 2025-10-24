package com.bandsyncapi.bandsyncapi.api.v1.services;

import java.util.List;

import com.bandsyncapi.bandsyncapi.api.v1.dto.musicalbands.MusicalBandsDto;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalBandsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.UsersModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.UsersMusicalBandsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.UsersMusicalBandsStatusModel;

/**
 * Defines methods for UsersMusicalBandsModel
 */
public interface UsersMusicalBandsService {

  /**
   * Save a UsersMusicalBandsModel
   * 
   * @param user - User
   * @param musicalBand - Musical Band
   * @param status - Status of the user in the band -> PENDING_ACTIVATION, ACTIVE, INACTIVE
   * @return - UsersMusicalBandsModel Object
   */
  UsersMusicalBandsModel save (UsersModel user, MusicalBandsModel musicalBand, UsersMusicalBandsStatusModel status);

  /**
   * Find all the bands a user is a part of
   * 
   * @param usersModel - UsersModel object
   * @return A list of UsersMusicalBandsModel
   */
  List<MusicalBandsDto> findByUser (UsersModel usersModel);
}
