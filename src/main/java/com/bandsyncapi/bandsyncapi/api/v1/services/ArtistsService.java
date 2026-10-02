package com.bandsyncapi.bandsyncapi.api.v1.services;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;

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
   * Finds all artists by musical band id and name.
   * 
   * @param musicalBandId - Musical Band id
   * @param name          - Artist name
   * @param page          - Page number for pagination
   * @param size          - Size of the page for pagination
   * @return - A list of Artists
   */
  public Page<ArtistsModel> findByMusicalBandIdAndName(UUID musicalBandId, String name, int page, int size);

  /**
   * Update Artist
   * 
   * @param id   - musical role id
   * @param name - Artist name
   */
  public void updateArtist(Integer id, String name);

  /**
   * Deletes an artist from the database by its id.
   * and all related data to this artist id
   * 
   * @param id - Id of the artist to be deleted.
   */
  public void deleteById(Integer id);

  /**
   * Deletes artists by musical band id
   * 
   * @param musicalBandId - musical band id
   */
  public void deleteArtistsByMusicalBandId(UUID musicalBandId);
}
