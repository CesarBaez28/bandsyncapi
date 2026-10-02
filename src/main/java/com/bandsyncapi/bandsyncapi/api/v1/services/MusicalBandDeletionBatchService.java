package com.bandsyncapi.bandsyncapi.api.v1.services;

import java.util.List;
import java.util.UUID;

/**
 * Interface with the definition of the method to delete multiple musical bands
 */
public interface MusicalBandDeletionBatchService {
  /**
   * Deletes multiple musical bands by their ids
   * 
   * @param musicalBandIds - list of musical band ids to delete
   */
  void deleteMusicalBands(List<UUID> musicalBandIds);
}
