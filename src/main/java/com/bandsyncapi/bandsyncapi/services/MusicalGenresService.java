package com.bandsyncapi.bandsyncapi.services;

import java.util.List;
import java.util.Optional;

import com.bandsyncapi.bandsyncapi.models.MusicalGenresModel;

/*
 * This interface is a service for the musical_genres table in the database.
 * Defines methods for performing CRUD operations on the musical_genres table.
 */
public interface MusicalGenresService {

  public List<MusicalGenresModel> findAll();

  public Optional<MusicalGenresModel> findById(Integer id);

  public MusicalGenresModel save(MusicalGenresModel musicalGenresModel);

  public void deleteById(Integer id);
}
