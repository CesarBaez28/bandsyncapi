package com.bandsyncapi.bandsyncapi.api.v1.services.impl;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.bandsyncapi.bandsyncapi.api.v1.dto.songs.SongsPutDto;
import com.bandsyncapi.bandsyncapi.api.v1.models.SongsModel;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.RepertoiresSongsRepository;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.SongsRepository;
import com.bandsyncapi.bandsyncapi.api.v1.services.FilesService;
import com.bandsyncapi.bandsyncapi.api.v1.services.SongsService;
import com.bandsyncapi.bandsyncapi.utils.AwsUtils;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

/**
 * SongsService implementation
 */
@Service
@Slf4j
public class SongsServiceImpl implements SongsService {

  private final SongsRepository songsRepository;

  private final RepertoiresSongsRepository repertoiresSongsRepository;

  private final FilesService filesService;

  @Value("${aws.bucket.songs.directory}")
  private String awsSongsDirectory;

  /**
   * Constructor
   * 
   * @param songsRepository - Songs repository
   * @param repertoiresSongsRepository - repository for repertoires_songs table
   * @param filesService    - Service to upload files
   */
  public SongsServiceImpl(SongsRepository songsRepository, RepertoiresSongsRepository repertoiresSongsRepository,
      FilesService filesService) {
    this.songsRepository = songsRepository;
    this.repertoiresSongsRepository = repertoiresSongsRepository;
    this.filesService = filesService;
  }

  @Override
  @Transactional
  public SongsModel save(SongsModel song, MultipartFile file) throws IOException {
    log.info("Saving song: {}", song);

    song.setSheetMusic("");

    SongsModel savedSong = songsRepository.save(song);

    String fileUrl = filesService.uploadFile(file, awsSongsDirectory);

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
  public SongsModel findById(Integer id) {
    return songsRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Song not found with id: " + id));
  }

  @Override
  public List<SongsModel> findByMusicalBandId(UUID musicalBandId) {
    log.info("Finding songs by musical band id: {}", musicalBandId);
    return songsRepository.findByMusicalBandId(musicalBandId);
  }

  @Override
  public void updateSong(Integer id, SongsPutDto songPutDto, MultipartFile file) throws IOException {

    log.info("Updating song with id: {}", id);

    String fileUrl = filesService.uploadFile(file, awsSongsDirectory);
    String currentFile = songPutDto.sheetMusic();

    boolean hasNewFile = !fileUrl.isEmpty();
    boolean hasOldFile = currentFile != null && !currentFile.isEmpty();

    if (hasNewFile && hasOldFile) {
      String fileName = AwsUtils.getFileNameFromAwsUrl(currentFile);
      filesService.deleteFile(awsSongsDirectory + "/" + fileName);
    } else {
      fileUrl = currentFile;
    }

    int rowsUpdated = songsRepository.updateSong(id, songPutDto.name(), songPutDto.artist(), songPutDto.genre(),
        songPutDto.tonality(), songPutDto.link(), fileUrl);

    if (rowsUpdated == 0) {
      throw new EntityNotFoundException("Song not found with id: " + id);
    }
  }

  @Override
  public void deleteById(Integer id) {
    log.info("Deleting song by id: {}", id);

    log.info("Deleting relationship between repertoires and song");
    repertoiresSongsRepository.deleteBySongId(id);

    Optional<SongsModel> songOptional = songsRepository.findById(id);

    // Delete file song in background
    if (songOptional.isPresent() && !songOptional.get().getSheetMusic().isEmpty()) {
      CompletableFuture.runAsync(() -> {
        String fileName = AwsUtils.getFileNameFromAwsUrl(songOptional.get().getSheetMusic());
        String fileLocation = (awsSongsDirectory + "/" + fileName).trim();
        try {
          filesService.deleteFile(fileLocation);
          log.info("Deleted file: {}", fileLocation);
        } catch (Exception e) {
          log.error("Error deleting file {}: {}", fileLocation, e.getMessage(), e);
        }
        log.info("Files deleted in background (parallel execution)");
      });
    }

    songsRepository.deleteById(id);

    log.info("Song successfully deleted by id: {}", id);
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