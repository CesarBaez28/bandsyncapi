package com.bandsyncapi.bandsyncapi.api.v1.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bandsyncapi.bandsyncapi.api.v1.dto.musicalbands.MusicalBandsDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.musicalbands.MusicalBandsPostDto;
import com.bandsyncapi.bandsyncapi.api.v1.models.UsersModel;
import com.bandsyncapi.bandsyncapi.api.v1.services.MusicalBandsService;
import com.bandsyncapi.bandsyncapi.api.v1.services.UsersMusicalBandsService;
import com.bandsyncapi.bandsyncapi.response.ApiResponse;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * This is the controller to handle requests for the MusicalBandsModel.
 */
@RestController
@RequestMapping(path = "api/v1/musical-bands")
@Slf4j
public class MusicalBandsController {

  private final MusicalBandsService musicalBandsService;
  
  private final UsersMusicalBandsService usersMusicalBandsService;

  /**
   * Constructor for the MusicalBandsController class.
   * 
   * @param musicalBandsService      - Service with methods for performing CRUD
   *                                 operations on the musical_bands table.
   * @param usersMusicalBandsService - Service with methods for performing CRUD
   *                                 operations on the users_musical_bands table.
   */
  public MusicalBandsController(MusicalBandsService musicalBandsService, UsersMusicalBandsService usersMusicalBandsService) {
    this.musicalBandsService = musicalBandsService;
    this.usersMusicalBandsService = usersMusicalBandsService;
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

    log.info("Musical band registered successfully: {}", savedMusicalBandsModelDto);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new ApiResponse<>(true, "Banda musical registrada correctamente", savedMusicalBandsModelDto, null));
  }

  /**
   * Find all the bands a user is a part of
   * 
   * @param userId - user id
   * @return - An ApiResponse Object containing the list of MusicalBandsDto
   */
  @GetMapping("/findByUserId/{userId}")
  public ResponseEntity<ApiResponse<List<MusicalBandsDto>>> findByUserId(@PathVariable UUID userId) {
    var user = new UsersModel(userId);
    List<MusicalBandsDto> response = usersMusicalBandsService.findByUser(user);

    log.info("Musical bands found successfully");

    return ResponseEntity.status(HttpStatus.OK)
          .body(new ApiResponse<>(true, "Musical bands found successfully", response, null));
  }
}
