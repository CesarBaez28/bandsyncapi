package com.bandsyncapi.bandsyncapi.api.v1.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalGenresModel;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.MusicalGenresRepository;
import com.bandsyncapi.bandsyncapi.api.v1.services.MusicalGenresService;

import jakarta.persistence.EntityNotFoundException;

/**
 * This class is a service implementation of the MusicalGenresService interface.
 */
@Service
public class MusicalGenresServiceImpl implements MusicalGenresService {

  private final MusicalGenresRepository musicalGenresRepository;

  /**
   * Constructor for the MusicalGenresServiceImpl class.
   * @param musicalGenresRepository - Repository with methods for performing CRUD operations on the musical_genres table.
   */
  public MusicalGenresServiceImpl(MusicalGenresRepository musicalGenresRepository) {
    this.musicalGenresRepository = musicalGenresRepository;
  }

  @Override
  public List<MusicalGenresModel> findAll() {
    return musicalGenresRepository.findAll();
  }

  @Override
  public List<MusicalGenresModel> findByMusicalBandId(UUID id) {
    return musicalGenresRepository.findByMusicalBandId(id);
  }

  @Override
  public Optional<MusicalGenresModel> findById(Integer id) {
    return musicalGenresRepository.findById(id);
  }

  @Override
  public MusicalGenresModel save(MusicalGenresModel musicalGenresModel) {
    return musicalGenresRepository.save(musicalGenresModel);
  }

  @Override
  public void updateGenreName(Integer id, String name) {
    int rowsUpdated = musicalGenresRepository.updateGenreName(id, name);

    if (rowsUpdated == 0) {
      throw new EntityNotFoundException("No se encontró un género musical con ese id");
    }
  }

  @Override
  public void deleteById(Integer id) {
    musicalGenresRepository.deleteById(id);
  }
}
