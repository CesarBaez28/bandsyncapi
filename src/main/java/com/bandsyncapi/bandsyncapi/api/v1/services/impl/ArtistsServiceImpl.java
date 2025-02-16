package com.bandsyncapi.bandsyncapi.api.v1.services.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.bandsyncapi.bandsyncapi.api.v1.models.ArtistsModel;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.ArtistsRepository;
import com.bandsyncapi.bandsyncapi.api.v1.services.ArtistsService;

import jakarta.persistence.EntityNotFoundException;

/**
 * Implementation of the ArtistsService interface
 */
@Service
public class ArtistsServiceImpl implements ArtistsService {

  private final ArtistsRepository artistsRepository;

  public ArtistsServiceImpl (ArtistsRepository artistsRepository) {
    this.artistsRepository = artistsRepository;
  }

  @Override
  public ArtistsModel save(ArtistsModel artistsModel) {
    return artistsRepository.save(artistsModel);
  }

  @Override
  public List<ArtistsModel> findByMusicalBandId(UUID id) {
    return artistsRepository.findByMusicalBandId(id);
  }

  @Override
  public void updateArtist(Integer id, String name) {
    int rowsUpdated =  artistsRepository.updateArtistName(id, name);

    if (rowsUpdated == 0) {
      throw new EntityNotFoundException("No se encontró un Artista con ese id"); 
    }
  }

  @Override
  public void deleteById(Integer id) {
    artistsRepository.deleteById(id);
  } 
}
