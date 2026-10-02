package com.bandsyncapi.bandsyncapi.api.v1.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.bandsyncapi.bandsyncapi.api.v1.models.RepertoiresModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.RepertoiresSongsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.SongsModel;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.RepertoiresSongsRepository;
import com.bandsyncapi.bandsyncapi.api.v1.services.RepertoiresSongsService;

import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of RepertoiresSongsService
 * 
 */
@Service
@Slf4j
public class RepertoiresSongsServiceImpl implements RepertoiresSongsService {

  private final RepertoiresSongsRepository repertoiresSongsRepository;

  /**
   * Constructor of the class
   * 
   * @param repertoiresSongsRepository - Repository to perform crud operations on
   *                                   RepertoiresSongsModel
   */
  public RepertoiresSongsServiceImpl(RepertoiresSongsRepository repertoiresSongsRepository) {
    this.repertoiresSongsRepository = repertoiresSongsRepository;
  }

  @Override
  public List<RepertoiresSongsModel> saveAll(RepertoiresModel repertoiresModel, List<SongsModel> songsModels) {
    log.info("Saving all songs in repertoire {}", repertoiresModel.getId());

    List<RepertoiresSongsModel> repertoiresSongsModels = new ArrayList<>();

    for (SongsModel song : songsModels) {
      repertoiresSongsModels.add(new RepertoiresSongsModel(repertoiresModel, song, true));
    }

    return repertoiresSongsRepository.saveAll(repertoiresSongsModels);
  }

  @Override
  public List<SongsModel> findByRepertoireId(UUID repertoireId) {
    log.info("Finding repertoire songs by repertoire id {}", repertoireId);

    return repertoiresSongsRepository.findByRepertoireId(repertoireId).stream()
        .map(value -> value.getSong()).toList();
  }

  @Override
  public void updateRepertoireSongs(UUID repertoireId, List<SongsModel> songs) {
    log.info("Updating repertoire songs by repertoire id {}", repertoireId);

    List<RepertoiresSongsModel> existingRepertoireSongs = repertoiresSongsRepository.findByRepertoireId(repertoireId);

    List<Integer> songsToDelete = new ArrayList<>();

    // Getting songs that are no longer in the list
    for (RepertoiresSongsModel existingRepertoireSong : existingRepertoireSongs) {
      if (songs.stream().noneMatch(song -> song.getId().equals(existingRepertoireSong.getSong().getId()))) {
        songsToDelete.add(existingRepertoireSong.getSong().getId());
      }
    }

    // Delete songs that are no longer in the list
    if (!songsToDelete.isEmpty()) {
      log.info("Deleting songs {} from repertoire {}", songsToDelete, repertoireId);
      repertoiresSongsRepository.deleteBySongIds(songsToDelete);
    }

    // Getting new songs that are no present in the new list
    List<RepertoiresSongsModel> songsToAdd = new ArrayList<>();
    for (SongsModel song : songs) {
      if (existingRepertoireSongs.stream()
          .noneMatch(existingRepertoireSong -> existingRepertoireSong.getSong().getId().equals(song.getId()))) {
        songsToAdd.add(new RepertoiresSongsModel(new RepertoiresModel(repertoireId), song, true));
      }
    }

    // Add new songs to the repertoire
    if (!songsToAdd.isEmpty()) {
      log.info("Adding songs {} to repertoire {}", songsToAdd, repertoireId);
      repertoiresSongsRepository.saveAll(songsToAdd);
    }
  }
}
