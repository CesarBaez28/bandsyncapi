package com.bandsyncapi.bandsyncapi.api.v1.services;

import java.util.List;
import java.util.UUID;

import com.bandsyncapi.bandsyncapi.api.v1.models.ArtistsModel;

/**
 * This interface is a service for the artists table in the database.
 * Defines methods for performing CRUD operations on the artists table.
 */
public interface ArtistsService {

  /**
   * save a new musical Role
   * 
   * @param artistsModel - ArtistsModel
   * @return - The new Artist
   */
  public ArtistsModel save(ArtistsModel artistsModel);

  /**
   * finds Artist musical band id
   * 
   * @param id
   * @return
   */
  public List<ArtistsModel> findByMusicalBandId(UUID id);

  /**
   * Update Artist
   * 
   * @param id   - musical role id
   * @param name - Artist name
   */
  public void updateArtist(Integer id, String name);

  /**
   * Deletes an artist from the database by its id.
   * 
   * @param id - Id of the artist to be deleted.
   */
  public void deleteById(Integer id);
}
