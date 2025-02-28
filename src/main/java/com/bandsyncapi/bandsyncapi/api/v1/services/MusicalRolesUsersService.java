package com.bandsyncapi.bandsyncapi.api.v1.services;

import java.util.List;
import java.util.UUID;

import com.bandsyncapi.bandsyncapi.api.v1.projections.MusicalRolesUsersProjection;

/**
 * Interface that defines methods for MusicalRolesUsersModel
 */
public interface MusicalRolesUsersService {
  
  /**
   * Find all musical roles of the users of a specific musical band
   * 
   * @param musicalBandId - musicalBand id
   * @return - A MusicalRolesUsersModel List
   */
  List<MusicalRolesUsersProjection> findAllByMusicalBandId (UUID musicalBandId);
}
