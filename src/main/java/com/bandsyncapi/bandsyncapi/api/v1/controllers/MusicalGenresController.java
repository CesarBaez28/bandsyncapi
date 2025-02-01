package com.bandsyncapi.bandsyncapi.api.v1.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bandsyncapi.bandsyncapi.api.v1.dto.musicalgenres.MusicalGenreDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.musicalgenres.MusicalGenrePostDto;
import com.bandsyncapi.bandsyncapi.api.v1.mappers.MusicalGenresMapper;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalGenresModel;
import com.bandsyncapi.bandsyncapi.api.v1.services.MusicalGenresService;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * This is the controller to handle requests for the musical_genres table.
 */
@RestController
@RequestMapping(path = "api/v1/musical-genres")
public class MusicalGenresController {
  
  private final MusicalGenresService musicalGenresService;

  private final MusicalGenresMapper musicalGenresMapper;

  /**
   * Constructor for the MusicalGenresController class.
   * @param musicalGenresService - Service with methods for performing CRUD operations on the musical_genres table.
   */
  public MusicalGenresController(MusicalGenresService musicalGenresService, MusicalGenresMapper musicalGenresMapper) {
    this.musicalGenresService = musicalGenresService;
    this.musicalGenresMapper = musicalGenresMapper;
  }
  
  @PostMapping(path = "/save")
  public ResponseEntity<MusicalGenreDto> save(@RequestBody MusicalGenrePostDto musicalGenrePostDto, UUID id) {
    try {
      MusicalGenresModel musicalGenre = musicalGenresMapper.toEntity(musicalGenrePostDto);
      MusicalGenresModel savedEntity = musicalGenresService.save(musicalGenre);
      MusicalGenreDto responseDto = musicalGenresMapper.toDto(savedEntity);
      return ResponseEntity.ok(responseDto);
    } catch (Exception e) {
      return ResponseEntity.internalServerError().build();
    }
  }
  
}
