package com.bandsyncapi.bandsyncapi.api.v1.services.impl;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.bandsyncapi.bandsyncapi.api.v1.models.ArtistsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalGenresModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.SongsModel;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.SongsRepository;
import com.bandsyncapi.bandsyncapi.api.v1.services.FilesService;
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

  private final FilesService filesService;

  private static final String SONGS_DIRECTORY = "bandsync/songs";

  /**
   * Constructor
   * 
   * @param songsRepository - Songs repository
   * @param filesService - Service to upload files
   */
  public SongsServiceImpl(SongsRepository songsRepository, FilesService filesService) {
    this.songsRepository = songsRepository;
    this.filesService = filesService;
  }

  @Override
  @Transactional
  public SongsModel save(SongsModel song, MultipartFile file) throws IOException {
    log.info("Saving song: {}", song);

    song.setSheetMusic("");

    SongsModel savedSong = songsRepository.save(song);

    String fileUrl = filesService.uploadFile(file, SONGS_DIRECTORY);

    if (!fileUrl.isEmpty()) {
      savedSong.setSheetMusic(fileUrl);
      songsRepository.updateSheetMusicById(savedSong.getId(), fileUrl);
    }

    return savedSong;
  }

  @Override
  public Page<SongsModel> find(UUID musicalBandId, String term, int page, int size) {
    log.info("Finding songs by musical band id: {} and term: {}", musicalBandId, term);
    return songsRepository.find(musicalBandId, term, PageRequest.of(page, size, Sort.by("name")));
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

  @Override
  public void deleteByArtistId(Integer artistId) {
    log.info("Deleting by artist id: {}", artistId);

    songsRepository.deleteByArtistId(artistId);

    log.info("Songs deleted successfully by artist id: {}", artistId);
  }

  @Override
  public void deleteByGenreId(Integer genreId) {
    log.info("Deleting by genre id: {}", genreId);

    songsRepository.deleteByGenreId(genreId);

    log.info("Songs deleted successfully by genre id: {}", genreId);
  }
}