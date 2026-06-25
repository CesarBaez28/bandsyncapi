package com.bandsyncapi.bandsyncapi.api.v1.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bandsyncapi.bandsyncapi.api.v1.constants.UserPermissions;
import com.bandsyncapi.bandsyncapi.api.v1.dto.repertoires.RepertoiresDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.repertoires.RepertoiresPostDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.repertoires.RepertoiresPutDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.songs.SongsDto;
import com.bandsyncapi.bandsyncapi.api.v1.mappers.RepertoiresMapper;
import com.bandsyncapi.bandsyncapi.api.v1.mappers.SongsMapper;
import com.bandsyncapi.bandsyncapi.api.v1.models.RepertoiresModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.SongsModel;
import com.bandsyncapi.bandsyncapi.api.v1.services.RepertoiresService;
import com.bandsyncapi.bandsyncapi.api.v1.services.RepertoiresSongsService;
import com.bandsyncapi.bandsyncapi.constants.Constants;
import com.bandsyncapi.bandsyncapi.response.ApiResponse;
import com.bandsyncapi.bandsyncapi.response.PagedData;
import com.bandsyncapi.bandsyncapi.security.RateLimited;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping(path = "api/v1/repertoires")
@Slf4j
@RateLimited(capacity = Constants.RATE_LIMIT_CAPACITY, refillTokens = Constants.RATE_LIMIT_TOKENS, refillMinutes = Constants.RATE_LIMIT_MINUTES)
public class RepertoiresController {

  private final RepertoiresService repertoiresService;

  private final RepertoiresMapper repertoiresMapper;

  private final SongsMapper songsMapper;

  private final RepertoiresSongsService repertoiresSongsService;

  private static final int PAGE_SIZE = 10;

  /**
   * Constructor
   * 
   * @param repertoiresService      - Service with methods for performing CRUD
   *                                operations on the repertoires table.
   * @param repertoiresSongsService Service with methods for performing CRUD
   *                                operations on the repertoires_songs table.
   * @param repertoiresMapper       - Mapper to convert between RepertoiresModel
   *                                and RepertoiresDto.
   */
  public RepertoiresController(RepertoiresService repertoiresService, RepertoiresSongsService repertoiresSongsService,
      RepertoiresMapper repertoiresMapper, SongsMapper songsMapper) {
    this.repertoiresService = repertoiresService;
    this.repertoiresSongsService = repertoiresSongsService;
    this.repertoiresMapper = repertoiresMapper;
    this.songsMapper = songsMapper;
  }

  /**
   * Save a repertoire
   * 
   * @param repertoiresPostDto - request to save the repertoire
   * @return - An ApiResponse object
   */
  @PostMapping("/save")
  @PreAuthorize("hasRole('" + UserPermissions.ADD_REPERTOIRE + "')")
  @RateLimited(capacity = 20, refillTokens = 20, refillMinutes = 1)
  public ResponseEntity<ApiResponse<Void>> save(@Valid @RequestBody RepertoiresPostDto repertoiresPostDto) {
    RepertoiresModel repertoiresModel = repertoiresMapper.toModel(repertoiresPostDto);
    RepertoiresModel repertoiresModelSaved = repertoiresService.save(repertoiresModel);
    repertoiresSongsService.saveAll(repertoiresModelSaved, repertoiresPostDto.songs());

    log.info("Repertoire saved successfully with id: {}", repertoiresModelSaved.getId());

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new ApiResponse<>(true, "Repertoire saved successfully", null, null));
  }

  /**
   * finds repertoires by musical band id
   * 
   * @param id - musical band id
   * @return - A RepertoiresDto list
   */
  @GetMapping("/findByMusicalBandId/{id}")
  public ResponseEntity<ApiResponse<List<RepertoiresDto>>> findByMusicalBandId(@PathVariable UUID id) {
    List<RepertoiresModel> repertoiresModelList = repertoiresService.findByMusicalBandId(id);
    List<RepertoiresDto> repertoiresDtoList = repertoiresMapper.toDtoList(repertoiresModelList);

    if (repertoiresModelList.isEmpty()) {
      log.info("Repertoires not found by id: {}" + id);
      return ResponseEntity.status(HttpStatus.OK)
          .body(new ApiResponse<>(true, "Repertoires not found", null, null));
    }

    log.info("Repertoires found by musical band id: {}", id);

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ApiResponse<>(true, "Repertoires found successfully", repertoiresDtoList, null));
  }

  /**
   * finds repertoires by musical band id and search term
   * 
   * @param musicalBandId - musical band id
   * @param query         - seach term
   * @param page          - page number
   * @return - A Page of type RepertoiresDto
   */
  @GetMapping("/find/{musicalBandId}")
  public ResponseEntity<ApiResponse<PagedData<RepertoiresDto>>> find(
      @PathVariable UUID musicalBandId,
      @RequestParam String query,
      @RequestParam(defaultValue = "0") int page) {

    page = Math.max(page - 1, 0); // convert to zero-based index and ensure non-negative

    Page<RepertoiresModel> repertoiresPage = repertoiresService.find(musicalBandId, query, page, PAGE_SIZE);

    List<RepertoiresDto> repertoiresDtoList = repertoiresMapper.toDtoList(repertoiresPage.getContent());

    PagedData<RepertoiresDto> pagedData = new PagedData<>(repertoiresDtoList, repertoiresPage);

    log.info("Repertoires found by musical band id: {} and term: {}", musicalBandId, query);

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ApiResponse<>(true, "Repertoires found successfully", pagedData, null));
  }

  /**
   * finds a repertoire by id
   * 
   * @param id - repertoire id
   * @return - A RepertoiresDto
   */
  @GetMapping("/findById/{id}")
  public ResponseEntity<ApiResponse<RepertoiresDto>> findById(@PathVariable UUID id) {
    RepertoiresModel repertoire = repertoiresService.findById(id);
    RepertoiresDto repertoireDto = repertoiresMapper.toDto(repertoire);

    log.info("Repertoire found with id: {}", id);

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ApiResponse<>(true, "Repertoire found successfully", repertoireDto, null));
  }

  /**
   * finds songs of a repertoire
   * 
   * @param id - repertoire id
   * @return - songs of the repertoire
   */
  @GetMapping("/findRepertoireSongs/{id}")
  public ResponseEntity<ApiResponse<List<SongsDto>>> findRepertoireSongs(@PathVariable UUID id) {
    List<SongsModel> repertoiresSongs = repertoiresSongsService.findByRepertoireId(id);
    List<SongsDto> songs = songsMapper.toDtoList(repertoiresSongs);

    log.info("Repertoire songs found with repertoire id: {}", id);

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ApiResponse<>(true, "Repertoire songs found successfully", songs, null));
  }

  /**
   * Update repertoire
   * 
   * @param id                - repertoire id
   * @param repertoiresPutDto - Data to be updated
   * @return - An ApiResponse object
   */
  @PutMapping("/updateRepertoire/{id}")
  @PreAuthorize("hasRole('" + UserPermissions.UPDATE_REPERTOIRE + "')")
  @RateLimited(capacity = 20, refillTokens = 20, refillMinutes = 1)
  public ResponseEntity<ApiResponse<Void>> updateRepertoire(@PathVariable UUID id,
      @Valid @RequestBody RepertoiresPutDto repertoiresPutDto) {
    repertoiresService.updateRepertoire(id, repertoiresPutDto);

    log.info("Repertoire updated successfully with id: {}", id);

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ApiResponse<>(true, "Repertoire updated successfully", null, null));
  }

  /**
   * deletes a repertoire by id
   * 
   * @param id - repertoire id
   * @return - An ApiResponse object
   */
  @DeleteMapping("/delete/{id}")
  @PreAuthorize("hasRole('" + UserPermissions.DELETE_REPERTOIRE + "')")
  @RateLimited(capacity = 10, refillTokens = 10, refillMinutes = 1)
  public ResponseEntity<ApiResponse<Void>> deleteById(@PathVariable UUID id) {
    repertoiresService.deleteById(id);

    log.info("Repertoire deleted successfully with id: {}", id);

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ApiResponse<>(true, "Repertoire deleted successfully", null, null));
  }
}
