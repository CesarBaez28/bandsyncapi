package com.bandsyncapi.bandsyncapi.api.v1.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bandsyncapi.bandsyncapi.api.v1.constants.UserPermissions;
import com.bandsyncapi.bandsyncapi.api.v1.dto.setlists.CreateSetListDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.setlists.SetListDetailsDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.setlists.SetListsDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.setlists.UpdateSetListDto;
import com.bandsyncapi.bandsyncapi.api.v1.mappers.SetListsMapper;
import com.bandsyncapi.bandsyncapi.api.v1.models.SetListsModel;
import com.bandsyncapi.bandsyncapi.api.v1.services.SetListsService;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * SetLists controller
 */
@RestController
@RequestMapping("/api/v1/setlists")
@Slf4j
@RateLimited(capacity = Constants.RATE_LIMIT_CAPACITY, refillTokens = Constants.RATE_LIMIT_TOKENS, refillMinutes = Constants.RATE_LIMIT_MINUTES)
public class SetListsController {

  private final SetListsService setListsService;

  private final SetListsMapper setListsMapper;

  private static final int PAGE_SIZE = 10;

  /**
   * Constructor for SetListsController.
   *
   * @param setListsService the service for managing set lists
   * @param setListsMapper  the mapper for converting between SetListsModel and
   *                        DTOs
   */
  public SetListsController(SetListsService setListsService, SetListsMapper setListsMapper) {
    this.setListsService = setListsService;
    this.setListsMapper = setListsMapper;
  }

  /**
   * Finds set lists by musical band id and a search term.
   * 
   * @param musicalBandId - musical band id
   * @param query         - search term
   * @param page          - page number
   * @return
   */
  @GetMapping("/musicalBandId/{musicalBandId}")
  public ResponseEntity<ApiResponse<PagedData<SetListsDto>>> findAllByMusicalBandId(
      @PathVariable UUID musicalBandId,
      @RequestParam String query,
      @RequestParam(defaultValue = "0") int page) {

    page = Math.max(page - 1, 0); // convert to zero-based index and ensure non-negative

    Page<SetListsModel> setListsPage = setListsService.findAllByMusicalBandId(musicalBandId, query, page, PAGE_SIZE);

    List<SetListsDto> setListsDto = setListsMapper.toDtoList(setListsPage.getContent());

    PagedData<SetListsDto> pagedData = new PagedData<>(setListsDto, setListsPage);

    log.info("Set lists found by musical band id: {} and term: {}", musicalBandId, query);

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ApiResponse<>(true, "Set lists found successfully", pagedData, null));
  }

  /**
   * Finds details of a set list by its id.
   * 
   * @param id - set list id
   * @return - ResponseEntity containing the API response with set list details
   *         data
   */
  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<SetListDetailsDto>> findSetListDetailsById(@PathVariable UUID id) {
    SetListDetailsDto setListDetailsDto = setListsService.findSetListDetailsById(id);

    log.info("Set list details found by id: {}", id);

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ApiResponse<>(true, "Set list details found successfully", setListDetailsDto, null));
  }

  /**
   * Creates a new set list for a given musical band.
   * 
   * @param musicalBandId    - musical band id
   * @param createSetListDto - data for the new set list
   * @return - ResponseEntity containing the API response
   */
  @PostMapping("/musicalBandId/{musicalBandId}")
  @PreAuthorize("hasRole('" + UserPermissions.ADD_SET_LIST + "')")
  public ResponseEntity<ApiResponse<Void>> createSetList(@PathVariable UUID musicalBandId,
      @RequestBody @Valid CreateSetListDto createSetListDto) {

    setListsService.saveSetList(createSetListDto);

    log.info("Set list created successfully");

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ApiResponse<>(true, "Set list created successfully", null, null));
  }

  /**
   * Updates an existing set list, including its sets and songs.
   *
   * @param id               set list id
   * @param updateSetListDto data to update
   * @return API response confirming the update
   */
  @PutMapping("/{id}")
  @PreAuthorize("hasRole('" + UserPermissions.UPDATE_SET_LIST + "')")
  public ResponseEntity<ApiResponse<Void>> updateSetList(@PathVariable UUID id,
      @RequestBody @Valid UpdateSetListDto updateSetListDto) {

    setListsService.updateSetList(id, updateSetListDto);

    log.info("Set list updated successfully with id: {}", id);

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ApiResponse<>(true, "Set list updated successfully", null, null));
  }

  /**
   * deletes a set list
   * 
   * @param id - set list id
   * @return API response confirming the delete
   */
  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('" + UserPermissions.DELETE_SET_LIST + "')")
  public ResponseEntity<ApiResponse<Void>> deleteSetList(@PathVariable UUID id) {

    setListsService.deleteSetList(id);

    log.info("Set list deleted successfully with id: {}", id);

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ApiResponse<>(true, "Set list deleted successfully", null, null));
  }

}
