package com.bandsyncapi.bandsyncapi.api.v1.services;

import java.io.IOException;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;
import com.bandsyncapi.bandsyncapi.api.v1.dto.musicalbands.MusicalBandPutDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.musicalbands.MusicalBandsDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.musicalbands.MusicalBandsPostDto;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalBandsModel;

/**
 * This interface is a service for the musical_bands table in the database.
 * Defines methods for performing CRUD operations on the musical_bands table.
 */
public interface MusicalBandsService {

  /**
   * Finds a musical band by its id.
   * @param id - Id of the musical band to be found.
   * @return - the musical band found
   */
  public MusicalBandsDto findById(UUID id);

  /**
   * Saves a musical band to the database.
   * @param musicalBandsModel - Musical band to be saved.
   * @return - The saved musical band.
   */
  public MusicalBandsModel save(MusicalBandsModel musicalBandsModel);

  /**
   * Register a new musical band
   *  
   * @param musicalBandsPostDto - Post request for creating the new MusicalBand
   * @param imageFile - musicalband logo
   * @return - The new musical band
   */
  public MusicalBandsDto registerMusicalBand (MusicalBandsPostDto musicalBandsPostDto, MultipartFile imageFile) throws IOException;

  /**
   * Update a musical band
   * 
   * @param musicalBandId - musical band id
   * @param musicalBandPutDto - new musical band data to be updated
   * @param imageFile - new logo to be updated
   * @return - a MusicalBandsDto object with the new values 
   * @throws IOException
   */
  public MusicalBandPutDto update (UUID musicalBandId, MusicalBandPutDto musicalBandPutDto,MultipartFile imageFile) throws IOException;

  /**
   * Check if the musical band exists by id
   * 
   * @param id - Musical band id
   * @return - true if exists, false otherwise
   */
  boolean existsById(UUID id);

  /**
   * find a musical band by hyphenated name
   * 
   * @param name -  musical band name
   * @return  -  the musical band found
   */
  public MusicalBandsDto findByHyphenatedName (String name);

  /**
   * Update logo by id
   * 
   * @param musicalBandId - Musical band id
   * @param logo - Logo
   */
  void updateLogoById (UUID musicalBandId, String logo);
}
