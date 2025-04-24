package com.bandsyncapi.bandsyncapi.api.v1.services.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.bandsyncapi.bandsyncapi.api.v1.models.ArtistsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalGenresModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.SongsModel;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.SongsRepository;
import com.bandsyncapi.bandsyncapi.api.v1.services.SongsService;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

/**
 * SongsService implementation
 */
@Service
@Slf4j
public class SongsServiceImpl implements SongsService {

  private final SongsRepository songsRepository;

  /**
   * Constructor
   * 
   * @param songsRepository - Songs repository
   */
  public SongsServiceImpl(SongsRepository songsRepository) {
    this.songsRepository = songsRepository;
  }

  @Override
  public SongsModel save(SongsModel song) {
    log.info("Saving song: {}", song);
    return songsRepository.save(song);
  }

  @Override
  public List<SongsModel> findByMusicalBandId(UUID musicalBandId) {
    log.info("Finding songs by musical band id: {}", musicalBandId);
    return songsRepository.findByMusicalBandId(musicalBandId);
  }

  @Override
  public void updateSong(Integer id, String name, ArtistsModel artist, MusicalGenresModel genre, String tonality,
      String link,
      String sheetMusic) {

    log.info("Updating song with id: {}", id);

    int rowsUpdated = songsRepository.updateSong(id, name, artist, genre, tonality, link, sheetMusic);

    if (rowsUpdated == 0) {
      throw new EntityNotFoundException("Song not found with id: " + id);
    }
  }

}
