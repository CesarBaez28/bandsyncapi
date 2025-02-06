package com.bandsyncapi.bandsyncapi.api.v1.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bandsyncapi.bandsyncapi.api.v1.dto.musicalgenres.MusicalGenreDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.musicalgenres.MusicalGenrePostDto;
import com.bandsyncapi.bandsyncapi.api.v1.mappers.MusicalGenresMapper;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalGenresModel;
import com.bandsyncapi.bandsyncapi.api.v1.services.MusicalGenresService;
import com.bandsyncapi.bandsyncapi.response.ApiResponse;

import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


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
   * 
   * @param musicalGenresService - Service with methods for performing CRUD
   *                             operations on the musical_genres table.
   */
  public MusicalGenresController(MusicalGenresService musicalGenresService, MusicalGenresMapper musicalGenresMapper) {
    this.musicalGenresService = musicalGenresService;
    this.musicalGenresMapper = musicalGenresMapper;
  }

  /**
   * Saves a new musical genre in the database
   * 
   * @param musicalGenrePostDto - The request body to be sent to the client
   * @return - An object ApiResponse with the new musical genre
   */
  @PostMapping(path = "/save")
  public ResponseEntity<ApiResponse<MusicalGenreDto>> saveMusicalGenre(@Valid @RequestBody MusicalGenrePostDto musicalGenrePostDto) {
    MusicalGenresModel musicalGenre = musicalGenresMapper.toModel(musicalGenrePostDto);
    MusicalGenresModel savedEntity = musicalGenresService.save(musicalGenre);
    MusicalGenreDto responseDto = musicalGenresMapper.toDto(savedEntity);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new ApiResponse<>(true, "Género musical guardado exitosamente.", responseDto, null));
  }

  @GetMapping("/findBymusicalBandId/{musicalBandId}")
  public ResponseEntity<ApiResponse<List<MusicalGenreDto>>> findByMusicalBandId(@PathVariable UUID musicalBandId) {
    List<MusicalGenresModel> musicalgenres = musicalGenresService.findByMusicalBandId(musicalBandId);
    List<MusicalGenreDto> musicalGenreResponse = musicalGenresMapper.toDtoList(musicalgenres);

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ApiResponse<>(true, "Datos encontrados correctamente", musicalGenreResponse, null));
  }
}
