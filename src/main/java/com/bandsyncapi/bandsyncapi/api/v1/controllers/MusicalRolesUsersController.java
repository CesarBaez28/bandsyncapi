package com.bandsyncapi.bandsyncapi.api.v1.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bandsyncapi.bandsyncapi.api.v1.dto.users.MusicalRolesUsersDto;
import com.bandsyncapi.bandsyncapi.api.v1.mappers.MusicalRolesUsersMapper;
import com.bandsyncapi.bandsyncapi.api.v1.projections.MusicalRolesUsersProjection;
import com.bandsyncapi.bandsyncapi.api.v1.services.MusicalRolesUsersService;
import com.bandsyncapi.bandsyncapi.response.ApiResponse;

import java.util.List;
import java.util.UUID;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Controller to handle request fo MusicalRolesUsersModel
 */
@RestController
@RequestMapping(path = "api/v1/musical-roles-users")
public class MusicalRolesUsersController {

  private final MusicalRolesUsersService musicalRolesUsersService;

  private final MusicalRolesUsersMapper musicalRolesUsersMapper;

  /**
   * Constructor
   * 
   * @param musicalRolesUsersService - MusicalRolesUsers service
   * @param musicalRolesUsersMapper  - MusicalRolesUsers mapper
   */
  public MusicalRolesUsersController(MusicalRolesUsersService musicalRolesUsersService,
      MusicalRolesUsersMapper musicalRolesUsersMapper) {
    this.musicalRolesUsersService = musicalRolesUsersService;
    this.musicalRolesUsersMapper = musicalRolesUsersMapper;
  }

  @GetMapping("/findAllByMusicalBandId/{musicalBandId}")
  public ResponseEntity<ApiResponse<List<MusicalRolesUsersDto>>> findAllByMusicalBandId(
      @PathVariable UUID musicalBandId) {

    List<MusicalRolesUsersProjection> musicalRolesUsersProjections = musicalRolesUsersService
        .findAllByMusicalBandId(musicalBandId);

    if (musicalRolesUsersProjections.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(new ApiResponse<>(false, "No se encontraron datos", null, null));
    }

    List<MusicalRolesUsersDto> response = musicalRolesUsersMapper.toDtoList(musicalRolesUsersProjections);

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ApiResponse<>(true, "Datos encontrados", response, null));
  }
}
