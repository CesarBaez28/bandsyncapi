package com.bandsyncapi.bandsyncapi.api.v1.services.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.bandsyncapi.bandsyncapi.api.v1.services.MusicalBandDeletionBatchService;
import com.bandsyncapi.bandsyncapi.api.v1.services.MusicalBandDeletionService;

import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of MusicalBandDeletionBatchService
 */
@Service
@Slf4j
public class MusicalBandDeletionBatchServiceImpl implements MusicalBandDeletionBatchService {

  private final MusicalBandDeletionService musicalBandDeletionService;

  public MusicalBandDeletionBatchServiceImpl(MusicalBandDeletionService musicalBandDeletionService) {
    this.musicalBandDeletionService = musicalBandDeletionService;
  }

  @Override
  public void deleteMusicalBands(List<UUID> musicalBandIds) {
    log.info("Deleting musical bands: {}", musicalBandIds);

    for (UUID musicalBandId : musicalBandIds) {
      try {
        musicalBandDeletionService.deleteMusuicalBand(musicalBandId);
      } catch (Exception e) {
        log.error("Error deleting musical band with id {}: {}", musicalBandId, e.getMessage());
      }
    }
  }
}
