package com.bandsyncapi.bandsyncapi.api.v1.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bandsyncapi.bandsyncapi.api.v1.dto.musicalbands.MusicalBandsDto;
import com.bandsyncapi.bandsyncapi.api.v1.mappers.MusicalBandsMapper;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalBandsModel;
import com.bandsyncapi.bandsyncapi.api.v1.services.MusicalBandsService;

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

  private final MusicalBandsMapper musicalBandsMapper;

  /**
   * Constructor for the MusicalBandsController class.
   * @param musicalBandsService - Service with methods for performing CRUD operations on the musical_bands table.
   * @param musicalBandsMapper - Mapper to convert between MusicalBandsModel and MusicalBandsDto.
   */
  public MusicalBandsController(MusicalBandsService musicalBandsService, MusicalBandsMapper musicalBandsMapper) {
    this.musicalBandsService = musicalBandsService;
    this.musicalBandsMapper = musicalBandsMapper;
  }

  /**
   * Saves a musical band to the database.
   * @param musicalBandsDto - Musical band to be saved.
   * @return - The saved musical band.
   */
  @PostMapping("/save")
  public ResponseEntity<MusicalBandsDto> saveMusicalBand(@RequestBody MusicalBandsDto musicalBandsDto) {
    try {
      MusicalBandsModel musicalBandsModel = musicalBandsMapper.toModel(musicalBandsDto);
      MusicalBandsModel savedMusicalBandsModel = musicalBandsService.save(musicalBandsModel);
      return new ResponseEntity<>(musicalBandsMapper.toDto(savedMusicalBandsModel), HttpStatus.CREATED);
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }
}
