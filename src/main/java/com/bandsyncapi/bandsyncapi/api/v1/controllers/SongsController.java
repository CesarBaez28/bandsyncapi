package com.bandsyncapi.bandsyncapi.api.v1.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bandsyncapi.bandsyncapi.api.v1.constants.UserPermissions;
import com.bandsyncapi.bandsyncapi.api.v1.dto.songs.SongsDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.songs.SongsPostDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.songs.SongsPutDto;
import com.bandsyncapi.bandsyncapi.api.v1.mappers.SongsMapper;
import com.bandsyncapi.bandsyncapi.api.v1.models.SongsModel;
import com.bandsyncapi.bandsyncapi.api.v1.services.SongsService;
import com.bandsyncapi.bandsyncapi.constants.Constants;
import com.bandsyncapi.bandsyncapi.response.ApiResponse;
import com.bandsyncapi.bandsyncapi.response.PagedData;
import com.bandsyncapi.bandsyncapi.security.RateLimited;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;

@RestController
@RequestMapping(path = "api/v1/songs")
@Slf4j
@RateLimited(capacity = Constants.RATE_LIMIT_CAPACITY, refillTokens = Constants.RATE_LIMIT_TOKENS, refillMinutes = Constants.RATE_LIMIT_MINUTES)
public class SongsController {

  private final SongsService songsService;

  private final SongsMapper songsMapper;

  private static final int PAGE_SIZE = 10;

  /**
   * Constructor of the class
   * 
   * @param songsService - Songs service with all business logic
   * @param songsMapper  - Mapper to convert between SongsModel and
   *                     SongsDto.
   */
  public SongsController(SongsService songsService, SongsMapper songsMapper) {
    this.songsService = songsService;
    this.songsMapper = songsMapper;
  }

  /**
   * save a song
   * 
   * @param songsPostDto
   * @return - SongsDto @see SongsDto
   */
  @PostMapping(path = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @PreAuthorize("hasRole('" + UserPermissions.ADD_SONG + "')")
  @RateLimited(capacity = 20, refillTokens = 20, refillMinutes = 1)
  public ResponseEntity<ApiResponse<SongsDto>> save(
      @Valid @RequestPart("song") SongsPostDto songsPostDto,
      @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {

    SongsModel songsModel = songsMapper.toModel(songsPostDto);
    SongsModel songsModelSaved = songsService.save(songsModel, file);
    SongsDto songsDtoResponse = songsMapper.toDto(songsModelSaved);

    log.info("Song saved: {}", songsDtoResponse);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new ApiResponse<>(true, "Song saved", songsDtoResponse, null));
  }

  /**
   * finds a song by id
   * 
   * @param id - song id
   * @return - A SongsDto
   */
  @GetMapping("/findById/{id}")
  public ResponseEntity<ApiResponse<SongsDto>> findById(@PathVariable Integer id) {
    SongsModel song = songsService.findById(id);
    SongsDto songDto = songsMapper.toDto(song);

    log.info("Song found with id: {}", id);

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ApiResponse<>(true, "Song found successfully", songDto, null));
  }

  /**
   * finds songs by musical band id
   * 
   * @param id - musical band id
   * @return - A SongsModel List
   */
  @GetMapping("/findByMusicalBandId/{id}")
  public ResponseEntity<ApiResponse<List<SongsDto>>> findByMusicalBandId(@PathVariable UUID id) {
    List<SongsModel> songsModelList = songsService.findByMusicalBandId(id);
    List<SongsDto> songsDtoResponse = songsMapper.toDtoList(songsModelList);

    log.info("Songs found by musicalBandId {}", id);

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ApiResponse<>(true, "Songs found", songsDtoResponse, null));
  }

  /**
   * finds songs by musical band id and search term
   * 
   * @param id    - musical band id
   * @param query - seach term
   * @param page  - page number
   * @return - A Page of type SongsModel
   */
  @GetMapping("/findByMusicalBandIdAndTerm/{id}")
  public ResponseEntity<ApiResponse<PagedData<SongsDto>>> find(
      @PathVariable UUID id,
      @RequestParam String query,
      @RequestParam(defaultValue = "0") int page) {

    page--; // convert to zero-based index

    Page<SongsModel> resultPage = songsService.find(id, query, page, PAGE_SIZE);

    List<SongsDto> songsDtoList = songsMapper.toDtoList(resultPage.getContent());

    PagedData<SongsDto> pagedData = new PagedData<>(songsDtoList, resultPage);

    log.info("Songs found by musical band id: {} and term: {}", id, query);

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ApiResponse<>(true, "Songs found successfully.", pagedData, null));
  }

  /**
   * Update song info
   * 
   * @param id          - song id
   * @param songsPutDto - Song info to be updated
   * @return
   */
  @PutMapping(path = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @PreAuthorize("hasRole('" + UserPermissions.UPDATE_SONG + "')")
  @RateLimited(capacity = 20, refillTokens = 20, refillMinutes = 1)
  public ResponseEntity<ApiResponse<Void>> update(
      @PathVariable Integer id,
      @Valid @RequestPart("song") SongsPutDto songsPutDto,
      @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {

    songsService.updateSong(id, songsPutDto, file);

    log.info("Song updated successfully: {}", songsPutDto);

    return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, "Song updated successfully", null, null));
  }

  /**
   * Deletes a song by id
   * 
   * @param id - Song id
   * @return An ApiResponse object
   */
  @DeleteMapping("/delete/{id}")
  @PreAuthorize("hasRole('" + UserPermissions.DELETE_SONG + "')")
  @RateLimited(capacity = 20, refillTokens = 20, refillMinutes = 1)
  public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Integer id) {
    songsService.deleteById(id);

    log.info("Song deleted successfully with id: {}", id);

    return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, "Song deleted successfully", null, null));
  }
}
