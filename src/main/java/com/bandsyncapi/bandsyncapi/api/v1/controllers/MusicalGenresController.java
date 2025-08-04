package com.bandsyncapi.bandsyncapi.api.v1.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bandsyncapi.bandsyncapi.api.v1.dto.musicalgenres.MusicalGenreDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.musicalgenres.MusicalGenrePostDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.musicalgenres.MusicalGenrePutDto;
import com.bandsyncapi.bandsyncapi.api.v1.mappers.MusicalGenresMapper;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalGenresModel;
import com.bandsyncapi.bandsyncapi.api.v1.services.MusicalGenresService;
import com.bandsyncapi.bandsyncapi.response.ApiResponse;
import com.bandsyncapi.bandsyncapi.response.PagedData;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * This is the controller to handle requests for the musical_genres table.
 */
@RestController
@RequestMapping(path = "api/v1/musical-genres")
@Slf4j
public class MusicalGenresController {

  private final MusicalGenresService musicalGenresService;

  private final MusicalGenresMapper musicalGenresMapper;

  private static final int PAGE_SIZE = 10;

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
  public ResponseEntity<ApiResponse<MusicalGenreDto>> saveMusicalGenre(
      @Valid @RequestBody MusicalGenrePostDto musicalGenrePostDto) {
    MusicalGenresModel musicalGenre = musicalGenresMapper.toModel(musicalGenrePostDto);
    MusicalGenresModel savedEntity = musicalGenresService.save(musicalGenre);
    MusicalGenreDto responseDto = musicalGenresMapper.toDto(savedEntity);

    log.info("Musical genre saved successfully: {}", responseDto);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new ApiResponse<>(true, "Musical genre successfully created.", responseDto, null));
  }

  /**
   * Finds musical genres by musical band id
   * 
   * @param musicalBandId - musical band id
   * @return - A list with all musical genres
   */
  @GetMapping("/findByMusicalBandId/{musicalBandId}")
  public ResponseEntity<ApiResponse<List<MusicalGenreDto>>> findByMusicalBandId(@PathVariable UUID musicalBandId) {
    List<MusicalGenresModel> musicalGenres = musicalGenresService.findByMusicalBandId(musicalBandId);
    List<MusicalGenreDto> musicalGenreResponse = musicalGenresMapper.toDtoList(musicalGenres);

    log.info("Musical genres found successfully: {}", musicalGenreResponse);

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ApiResponse<>(true, "Musical genres found successfully.", musicalGenreResponse, null));
  }

  /**
   * finds all musical genres by musical band and name
   * 
   * @param musicalBandId - Musical band id
   * @param query - Musical genre name
   * @param page - Page number for pagination
   * @return A Page List of MusicalGenreDto
   */
  @GetMapping("/findByMusicalBandIdAndName/{musicalBandId}")
  public ResponseEntity<ApiResponse<PagedData<MusicalGenreDto>>> getMethodName(@PathVariable UUID musicalBandId,
      @RequestParam String query,
      @RequestParam(defaultValue = "0") int page) {

    page--; // Convert to base zero

    Page<MusicalGenresModel> resultPage = musicalGenresService.findByMusicalBandIdAndName(musicalBandId, query, page,
        PAGE_SIZE);

    List<MusicalGenreDto> musicalGenreDtoList = musicalGenresMapper.toDtoList(resultPage.getContent());

    PagedData<MusicalGenreDto> pagedData = new PagedData<>(musicalGenreDtoList, resultPage);

    log.info("Musical Genres found by musical band id: {} and name: {}", musicalBandId, query);

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ApiResponse<>(true, "Musical genres found successfully.", pagedData, null));
  }

  /**
   * Update the name of a musical genre
   * 
   * @param id                 - musical genre id
   * @param musicalGenrePutDto - the request body to update the musical genre
   * @return - An object ApiResponse indicating that the musical genre name was
   *         updated
   */
  @PutMapping("/updateMusicalGenreName/{id}")
  public ResponseEntity<ApiResponse<Void>> updateMusicalGenreName(@PathVariable Integer id,
      @Valid @RequestBody MusicalGenrePutDto musicalGenrePutDto) {
    musicalGenresService.updateGenreName(id, musicalGenrePutDto.name());

    log.info("Musical genre name updated successfully: {}", musicalGenrePutDto.name());

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ApiResponse<>(true, "Musical genre name updated successfully.", null, null));
  }

  /**
   * Delete a musical genre
   * 
   * @param id - musical genre id
   * @return - An object ApiResponse indicating that the musical genre was deleted
   */
  @DeleteMapping("/delete/{id}")
  public ResponseEntity<ApiResponse<Void>> deleteMusicalGenre(@PathVariable Integer id) {
    musicalGenresService.deleteById(id);

    log.info("Musical genre deleted successfully: {}", id);

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ApiResponse<>(true, "Musical genre deleted successfully.", null, null));
  }
}
