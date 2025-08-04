package com.bandsyncapi.bandsyncapi.api.v1.services.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.bandsyncapi.bandsyncapi.api.v1.models.ArtistsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.SongsModel;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.ArtistsRepository;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.RepertoiresSongsRepository;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.SongsRepository;
import com.bandsyncapi.bandsyncapi.api.v1.services.ArtistsService;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of the ArtistsService interface
 */
@Service
@Slf4j
public class ArtistsServiceImpl implements ArtistsService {

  private final ArtistsRepository artistsRepository;

  private final SongsRepository songsRepository;

  private final RepertoiresSongsRepository repertoiresSongsRepository;

  /**
   * Constructor
   * 
   * @param artistsRepository - Artists repository
   * @param songsRepository - Songs repository
   * @param repertoiresSongsRepository - Repertoires songs repository
   */
  public ArtistsServiceImpl(ArtistsRepository artistsRepository, SongsRepository songsRepository,
      RepertoiresSongsRepository repertoiresSongsRepository) {
    this.artistsRepository = artistsRepository;
    this.songsRepository = songsRepository;
    this.repertoiresSongsRepository = repertoiresSongsRepository;
  }

  @Override
  public ArtistsModel save(ArtistsModel artistsModel) {
    log.info("Saving artist: {}", artistsModel);
    return artistsRepository.save(artistsModel);
  }

  @Override
  public List<ArtistsModel> findByMusicalBandId(UUID id) {
    log.info("Finding artists by musical band id: {}", id);
    return artistsRepository.findByMusicalBandId(id);
  }

  @Override
  public void updateArtist(Integer id, String name) {
    log.info("Updating artist with id: {} and name: {}", id, name);

    int rowsUpdated = artistsRepository.updateArtistName(id, name);

    if (rowsUpdated == 0) {
      log.error("Artist not found with id: {}", id);
      throw new EntityNotFoundException("Artist not found with id: " + id);
    }
  }

  @Override
  public void deleteById(Integer id) {
    log.info("Deleting artist with id: {}", id);

    log.info("finding songs related to the artist id: {}", id);

    List<SongsModel> songs = songsRepository.findByArtistId(id);

    log.info("Deleting relationship between repertoires and songs");

    repertoiresSongsRepository.deleteBySongs(songs);

    log.info("Deleting songs related to the artist id: {}", id);

    songsRepository.deleteByArtistId(id);

    artistsRepository.deleteById(id);

    log.info("Artist successfully deleted by id: {}", id);
  }

  @Override
  public Page<ArtistsModel> findByMusicalBandIdAndName(UUID musicalBandId, String name, int page, int size) {
    log.info("Finding artists by musical band id: {} and name: {}", musicalBandId, name);
    return artistsRepository.findByMusicalBandIdAndName(musicalBandId, name,
        PageRequest.of(page, size, Sort.by("name")));
  }
}
