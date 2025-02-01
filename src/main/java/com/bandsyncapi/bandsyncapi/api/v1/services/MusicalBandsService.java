package com.bandsyncapi.bandsyncapi.api.v1.services;

import java.util.Optional;
import java.util.UUID;

import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalBandsModel;

/**
 * This interface is a service for the musical_bands table in the database.
 * Defines methods for performing CRUD operations on the musical_bands table.
 */
public interface MusicalBandsService {

  /**
   * Finds a musical band by its id.
   * @param id - Id of the musical band to be found.
   * @return - An Optional with the musical band if found, or an empty Optional if not found.
   */
  public Optional<MusicalBandsModel> findById(UUID id);

  /**
   * Saves a musical band to the database.
   * @param musicalBandsModel - Musical band to be saved.
   * @return - The saved musical band.
   */
  public MusicalBandsModel save(MusicalBandsModel musicalBandsModel);
}
