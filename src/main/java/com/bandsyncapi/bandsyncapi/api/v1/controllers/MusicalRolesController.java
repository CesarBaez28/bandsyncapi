package com.bandsyncapi.bandsyncapi.api.v1.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bandsyncapi.bandsyncapi.api.v1.dto.musicalroles.MusicalRolesDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.musicalroles.MusicalRolesPostDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.musicalroles.MusicalRolesPutDto;
import com.bandsyncapi.bandsyncapi.api.v1.mappers.MusicalRolesMapper;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalRolesModel;
import com.bandsyncapi.bandsyncapi.api.v1.services.MusicalRolesService;
import com.bandsyncapi.bandsyncapi.response.ApiResponse;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

/**
 * This is the controller to handle requests for the musical_roles table.
 */
@RestController
@RequestMapping(path = "api/v1/musical-roles")
@Slf4j
public class MusicalRolesController {

  private final MusicalRolesService musicalRolesService;

  private final MusicalRolesMapper musicalRolesMapper;

  /**
   * Constructor of the class
   * 
   * @param musicalRolesService - Service with methods for performing CRUD
   *                            operations on the musical_roles table.
   */
  public MusicalRolesController(MusicalRolesService musicalRolesService, MusicalRolesMapper musicalRolesMapper) {
    this.musicalRolesService = musicalRolesService;
    this.musicalRolesMapper = musicalRolesMapper;
  }

  /**
   * Save a new musical role
   * 
   * @param musicalRolesPostDto - Request body to save the new musical role
   * @return -An object ApiResponse with the new musical role
   */
  @PostMapping("/save")
  public ResponseEntity<ApiResponse<MusicalRolesDto>> save(
      @Valid @RequestBody MusicalRolesPostDto musicalRolesPostDto) {
    MusicalRolesModel musicalRolesModel = musicalRolesMapper.toModel(musicalRolesPostDto);
    MusicalRolesModel savedMusicalRolesModel = musicalRolesService.save(musicalRolesModel);
    MusicalRolesDto musicalRolesDtoResponse = musicalRolesMapper.toDto(savedMusicalRolesModel);

    log.info("Musical role saved successfully: {}", musicalRolesDtoResponse);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new ApiResponse<>(true, "New role saved successfully", musicalRolesDtoResponse, null));
  }

  /**
   * finds musical roles by musicalBandId
   * 
   * @param musicalBandId - musical band id
   * @return A list with all musical roles
   */
  @GetMapping("/findByMusicalBandId/{musicalBandId}")
  public ResponseEntity<ApiResponse<List<MusicalRolesDto>>> findByMusicalBandId(@PathVariable UUID musicalBandId) {
    List<MusicalRolesModel> musicalRolesModels = musicalRolesService.findByMusicalBandId(musicalBandId);
    List<MusicalRolesDto> musicalRolesDtoListResponse = musicalRolesMapper.toDtoList(musicalRolesModels);

    if (musicalRolesDtoListResponse.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(new ApiResponse<>(false, "No musical roles found", null, null));
    }

    log.info("Musical roles found successfully: {}", musicalRolesDtoListResponse);

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ApiResponse<>(true, "Musical roles found successfully", musicalRolesDtoListResponse, null));
  }

  /**
   * update musical role name
   * 
   * @param id                 - musical role id
   * @param musicalRolesPutDto - the request body to update the musical role
   * @return - An object ApiResponse indicating that the musical genre name was
   *         updated
   */
  @PutMapping("/updateMusicalRoleName/{id}")
  public ResponseEntity<ApiResponse<Void>> updateMusicalRoleName(@PathVariable Integer id,
      @Valid @RequestBody MusicalRolesPutDto musicalRolesPutDto) {
    musicalRolesService.updateMusicalRoleName(id, musicalRolesPutDto.name());

    log.info("Musical role name updated successfully: {}", musicalRolesPutDto.name());

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ApiResponse<>(true, "Musical role name updated successfully", null, null));
  }

  /**
   * Deletes a musical role by id
   * 
   * @param id - Musical role id
   * @return - An ApiResponse object indicating that the musical role was deleted
   */
  @DeleteMapping("/delete/{id}")
  public ResponseEntity<ApiResponse<Void>> deleteMusicalRole(@PathVariable Integer id) {
    musicalRolesService.deleteById(id);

    log.info("Musical role deleted successfully: {}", id);

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ApiResponse<>(true, "Musical role deleted successfully", null, null));
  }
}
