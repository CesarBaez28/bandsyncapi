package com.bandsyncapi.bandsyncapi.api.v1.services;

import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

/**
 * Class to manage asynchronously operations related to musicalbands
 */
public interface MusicalBandsServiceAsync {

  /**
   * Upload the logo of the band Asynchronously
   * 
   * @param musicalBandId - band id to update the url of the logo
   * @param imageFile     - File to upload
   */
  void uploadLogo(UUID musicalBandId, MultipartFile imageFile);
}
