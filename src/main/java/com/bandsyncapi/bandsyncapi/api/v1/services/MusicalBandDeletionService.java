package com.bandsyncapi.bandsyncapi.api.v1.services;

import java.util.UUID;

/**
 * Interface with the definition of the method to delete a musical band
 */
public interface MusicalBandDeletionService {

  /**
   * Deletes a musical band
   * 
   * @param musicalBandId - musical band id
   */
  void deleteMusuicalBand(UUID musicalBandId);
}
