package com.bandsyncapi.bandsyncapi.api.v1.services.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bandsyncapi.bandsyncapi.api.v1.repositories.MusicalBandsRepository;
import com.bandsyncapi.bandsyncapi.api.v1.services.FilesService;
import com.bandsyncapi.bandsyncapi.api.v1.services.MusicalBandsServiceAsync;

import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of MusicalBandsServiceAsync
 */
@Service
@Slf4j
public class MusicalBandsServiceAsyncImpl implements MusicalBandsServiceAsync {

  private final FilesService filesService;

  private final MusicalBandsRepository musicalBandsRepository;

  /**
   * Constructor
   * 
   * @param filesService           - Files Service to upload images
   * @param musicalBandsRepository - Musical bands repository
   */
  public MusicalBandsServiceAsyncImpl(FilesService filesService, MusicalBandsRepository musicalBandsRepository) {
    this.filesService = filesService;
    this.musicalBandsRepository = musicalBandsRepository;
  }

  @Value("${aws.bucket.logos.directory}")
  private String awsLogosDirectory;

  @Override
  @Async
  public void uploadLogo(UUID musicalBandId, MultipartFile imageFile) {
    try {
      log.info("Async process to upoload logo started for band {}", musicalBandId);

      String fileUrl = filesService.uploadFile(imageFile, awsLogosDirectory);

      if (fileUrl != null && !fileUrl.isBlank()) {
        musicalBandsRepository.updateLogoById(musicalBandId, fileUrl);
      }

      log.info("Async process to upload logo completed for musical band {}", musicalBandId);

    } catch (Exception e) {
      log.error("Error in async process to upload logo for band with id {}", musicalBandId, e);
    }
  }
}
