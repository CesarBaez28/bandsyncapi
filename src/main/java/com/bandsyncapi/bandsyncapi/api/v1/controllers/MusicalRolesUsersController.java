package com.bandsyncapi.bandsyncapi.api.v1.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bandsyncapi.bandsyncapi.api.v1.dto.musicalroles.MusicalRolesDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.users.MusicalRolesUsersDto;
import com.bandsyncapi.bandsyncapi.api.v1.mappers.MusicalRolesMapper;
import com.bandsyncapi.bandsyncapi.api.v1.mappers.MusicalRolesUsersMapper;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalRolesModel;
import com.bandsyncapi.bandsyncapi.api.v1.projections.MusicalRolesSingleUserProjection;
import com.bandsyncapi.bandsyncapi.api.v1.projections.MusicalRolesUsersProjection;
import com.bandsyncapi.bandsyncapi.api.v1.services.MusicalRolesUsersService;
import com.bandsyncapi.bandsyncapi.response.ApiResponse;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Controller to handle request fo MusicalRolesUsersModel
 */
@RestController
@RequestMapping(path = "api/v1/musical-roles-users")
public class MusicalRolesUsersController {

  private final MusicalRolesUsersService musicalRolesUsersService;

  private final MusicalRolesUsersMapper musicalRolesUsersMapper;

  private final MusicalRolesMapper musicalRolesMapper;

  /**
   * Constructor
   * 
   * @param musicalRolesUsersService - MusicalRolesUsers service
   * @param musicalRolesUsersMapper  - MusicalRolesUsers mapper
   * @param musicalRolesMapper - MusicalRolesMapper mapper
   */
  public MusicalRolesUsersController(MusicalRolesUsersService musicalRolesUsersService,
      MusicalRolesUsersMapper musicalRolesUsersMapper, MusicalRolesMapper musicalRolesMapper) {
    this.musicalRolesUsersService = musicalRolesUsersService;
    this.musicalRolesUsersMapper = musicalRolesUsersMapper;
    this.musicalRolesMapper = musicalRolesMapper;
  }

  /**
   * finds musical roles of all users
   * 
   * @param musicalBandId
   * @return
   */
  @GetMapping("/findAllByMusicalBandId/{musicalBandId}")
  public ResponseEntity<ApiResponse<List<MusicalRolesUsersDto>>> findAllByMusicalBandId(
      @PathVariable UUID musicalBandId) {

    List<MusicalRolesUsersProjection> musicalRolesUsersProjections = musicalRolesUsersService
        .findAllByMusicalBandId(musicalBandId);

    List<MusicalRolesUsersDto> response = musicalRolesUsersMapper.toDtoList(musicalRolesUsersProjections);

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ApiResponse<>(true, "Datos encontrados", response, null));
  }

  /**
   * Finds musical roles of a specific user
   * 
   * @param musicalBandId - musical band id
   * @param userId        - user id
   * @return - An ApiResponse object with the data
   */
  @GetMapping("/findMusicalRolesUser/{musicalBandId}/{userId}")
  public ResponseEntity<ApiResponse<List<MusicalRolesSingleUserProjection>>> findMusicalRolesUser(
      @PathVariable UUID musicalBandId, @PathVariable UUID userId) {

    List<MusicalRolesSingleUserProjection> response = musicalRolesUsersService.findMusicalRolesUser(musicalBandId,
        userId);

    if (response.isEmpty()) {
      return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(false, "No se encontraron datos", response, null));
    }

    return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, "Datos encontrados", response, null));
  }

  /**
   * Assing musical roles to a user in a specific musical band
   * 
   * @param musicalBandId - musical band id
   * @param userId - user id
   * @param musicalRoles - MusicalRolesDto List
   * @return An ApiResponse object
   */
  @PostMapping("/assingMusicalRoles/{musicalBandId}/{userId}")
  public ResponseEntity<ApiResponse<Void>> assingMusicalRoles(@PathVariable UUID musicalBandId,
      @PathVariable UUID userId, @RequestBody List<MusicalRolesDto> musicalRoles) {

    List<MusicalRolesModel> musicalRolesModelList = musicalRolesMapper.toModelList(musicalRoles);
    
    musicalRolesUsersService.assignMusicalRolesUser(userId, musicalBandId, musicalRolesModelList);

    return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, "Roles musicales asignados", null, null));
  }

}
