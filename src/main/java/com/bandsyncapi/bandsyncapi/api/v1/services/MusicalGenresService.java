package com.bandsyncapi.bandsyncapi.api.v1.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalGenresModel;

/*
 * This interface is a service for the musical_genres table in the database.
 * Defines methods for performing CRUD operations on the musical_genres table.
 */
public interface MusicalGenresService {

  /**
   * Finds all musical genres in the database.
   * @return A list with all musical genres in the database.
   */
  public List<MusicalGenresModel> findAll();

  /**
   * Finds all musical genres by musical band id
   * @param id - musical band id
   * @return - A list of musical genres
   */
  public List<MusicalGenresModel>findByMusicalBandId(UUID id);

  /**
   * Finds a musical genre by its id.
   * @param id - Id of the musical genre to be found.
   * @return An Optional with the musical genre if found, or an empty Optional if not found.
   */
  public Optional<MusicalGenresModel> findById(Integer id);

  /**
   * Saves a musical genre to the database.
   * @param musicalGenresModel - Musical genre to be saved.
   * @return The saved musical genre.
   */
  public MusicalGenresModel save(MusicalGenresModel musicalGenresModel);

  /**
   * Update musical genre name
   * @param id - musical genre id
   * @param name - musical genre name
   */
  public void updateGenreName(Integer id, String name);

  /**
   * Deletes a musical genre from the database by its id.
   * @param id - Id of the musical genre to be deleted.
   */
  public void deleteById(Integer id);
}
