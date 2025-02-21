package com.bandsyncapi.bandsyncapi.api.v1.services;

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
}
