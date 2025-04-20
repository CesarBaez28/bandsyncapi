package com.bandsyncapi.bandsyncapi.api.v1.services.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.bandsyncapi.bandsyncapi.api.v1.models.ArtistsModel;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.ArtistsRepository;
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

  public ArtistsServiceImpl (ArtistsRepository artistsRepository) {
    this.artistsRepository = artistsRepository;
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
    
    int rowsUpdated =  artistsRepository.updateArtistName(id, name);

    if (rowsUpdated == 0) {
      log.error("Artist not found with id: {}", id);
      throw new EntityNotFoundException("Artist not found with id: " + id); 
    }
  }

  @Override
  public void deleteById(Integer id) {
    log.info("Deleting artist with id: {}", id);
    artistsRepository.deleteById(id);
  } 
}
