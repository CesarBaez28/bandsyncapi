package com.bandsyncapi.bandsyncapi.api.v1.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bandsyncapi.bandsyncapi.api.v1.dto.musicalbands.MusicalBandsDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.musicalbands.MusicalBandsPostDto;
import com.bandsyncapi.bandsyncapi.api.v1.services.MusicalBandsService;
import com.bandsyncapi.bandsyncapi.response.ApiResponse;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * This is the controller to handle requests for the MusicalBandsModel.
 */
@RestController
@RequestMapping(path = "api/v1/musical-bands")
public class MusicalBandsController {

  private final MusicalBandsService musicalBandsService;

  /**
   * Constructor for the MusicalBandsController class.
   * 
   * @param musicalBandsService      - Service with methods for performing CRUD
   *                                 operations on the musical_bands table.
   */
  public MusicalBandsController(MusicalBandsService musicalBandsService) {
    this.musicalBandsService = musicalBandsService;
  }

  /**
   * Saves a musical band to the database.
   * 
   * @param musicalBandsPostDto - Musical band to be saved.
   * @return - The saved musical band.
   */
  @PostMapping("/save")
  public ResponseEntity<ApiResponse<MusicalBandsDto>> saveMusicalBand(
      @Valid @RequestBody MusicalBandsPostDto musicalBandsPostDto) {

    MusicalBandsDto savedMusicalBandsModelDto = musicalBandsService.registerMusicalBand(musicalBandsPostDto);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new ApiResponse<>(true, "Banda musical registrada correctamente", savedMusicalBandsModelDto, null));
  }
}
