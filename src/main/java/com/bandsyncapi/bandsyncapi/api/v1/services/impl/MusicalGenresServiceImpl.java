package com.bandsyncapi.bandsyncapi.api.v1.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalGenresModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.SongsModel;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.MusicalGenresRepository;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.RepertoiresSongsRepository;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.SongsRepository;
import com.bandsyncapi.bandsyncapi.api.v1.services.MusicalGenresService;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

/**
 * This class is a service implementation of the MusicalGenresService interface.
 */
@Service
@Slf4j
public class MusicalGenresServiceImpl implements MusicalGenresService {

  private final MusicalGenresRepository musicalGenresRepository;

  private final SongsRepository songsRepository;

  private final RepertoiresSongsRepository repertoiresSongsRepository;

  /**
   * Constructor for the MusicalGenresServiceImpl class.
   * 
   * @param musicalGenresRepository    - Repository with methods for performing
   *                                   CRUD
   *                                   operations on the musical_genres table.
   * @param songsRepository            - Repository for songs table.
   * @param repertoiresSongsRepository - Repository for repertoires_songs table.
   * 
   */
  public MusicalGenresServiceImpl(MusicalGenresRepository musicalGenresRepository, SongsRepository songsRepository,
      RepertoiresSongsRepository repertoiresSongsRepository) {
    this.musicalGenresRepository = musicalGenresRepository;
    this.songsRepository = songsRepository;
    this.repertoiresSongsRepository = repertoiresSongsRepository;
  }

  @Override
  public List<MusicalGenresModel> findAll() {
    log.info("Fetching all musical genres");
    return musicalGenresRepository.findAll();
  }

  @Override
  public List<MusicalGenresModel> findByMusicalBandId(UUID id) {
    log.info("Fetching all musical genres for the band with id: {}", id);
    return musicalGenresRepository.findByMusicalBandId(id);
  }

  @Override
  public Page<MusicalGenresModel> findByMusicalBandIdAndName(UUID musicalBandId, String name, int page, int size) {
    log.info("Fetching all musical genres for the band with id: {} and name: {}", musicalBandId, name);
    return musicalGenresRepository.findByMusicalBandIdAndName(musicalBandId, name,
        PageRequest.of(page, size, Sort.by("name")));
  }

  @Override
  public Optional<MusicalGenresModel> findById(Integer id) {
    log.info("Fetching musical genre by id: {}", id);
    return musicalGenresRepository.findById(id);
  }

  @Override
  public MusicalGenresModel save(MusicalGenresModel musicalGenresModel) {
    log.info("Saving musical genre: {}", musicalGenresModel);
    return musicalGenresRepository.save(musicalGenresModel);
  }

  @Override
  public void updateGenreName(Integer id, String name) {
    log.info("Updating musical genre with id: {} to name: {}", id, name);
    int rowsUpdated = musicalGenresRepository.updateGenreName(id, name);

    if (rowsUpdated == 0) {
      throw new EntityNotFoundException("Musical Genre not found with id: " + id);
    }
  }

  @Override
  public void deleteById(Integer id) {
    log.info("Deleting musical genre with id: {}", id);

    log.info("finding songs related to the genre id: {}", id);

    List<SongsModel> songs = songsRepository.findByGenreId(id);

    log.info("Deleting relationship between repertoires and songs");

    repertoiresSongsRepository.deleteBySongs(songs);

    log.info("Deleting songs related to the genre id: {}", id);

    songsRepository.deleteByGenreId(id);

    musicalGenresRepository.deleteById(id);

    log.info("Musical genre successfully deleted by id: {}", id);
  }

}
