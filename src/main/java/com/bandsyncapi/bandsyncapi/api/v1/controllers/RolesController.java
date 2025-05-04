package com.bandsyncapi.bandsyncapi.api.v1.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bandsyncapi.bandsyncapi.api.v1.dto.roles.RolesDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.roles.RolesPermissionsDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.roles.RolesPermissionsPutDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.roles.RolesPostDto;
import com.bandsyncapi.bandsyncapi.api.v1.mappers.RolesMapper;
import com.bandsyncapi.bandsyncapi.api.v1.models.RolesModel;
import com.bandsyncapi.bandsyncapi.api.v1.services.RolesService;
import com.bandsyncapi.bandsyncapi.response.ApiResponse;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;


/**
 * Controller to handle request for roles data
 */
@RestController
@RequestMapping(path = "api/v1/roles")
@Slf4j
public class RolesController {

  private final RolesService rolesService;

  private final RolesMapper rolesMapper;

  /**
   * Constructor
   * 
   * @param rolesService - Roles service
   * @param rolesMapper  - Roles mapper to convert between RolesModel and
   *                     RolesDto.
   */
  public RolesController(RolesService rolesService, RolesMapper rolesMapper) {
    this.rolesService = rolesService;
    this.rolesMapper = rolesMapper;
  }

  /**
   * Save a new role
   * 
   * 
   * @param rolesPostDto - RolesPostDto Object
   * @return RolesPermissionsDto object
   */
  @PostMapping("/save")
  public ResponseEntity<ApiResponse<RolesPermissionsDto>> save(@Valid @RequestBody RolesPostDto rolesPostDto) {

    RolesPermissionsDto response = rolesService.saveRoleAndPermissions(rolesPostDto);

    log.info("Role saved successfully: {}", response);

    return ResponseEntity.status(HttpStatus.CREATED)
    .body(new ApiResponse<>(true, "Role saved successfully.", response, null));
  }

  /**
   * finds all roles
   * 
   * @return A ApiResponse Object with the roles
   */
  @GetMapping("/findAll")
  public ResponseEntity<ApiResponse<List<RolesDto>>> findAll() {
    List<RolesModel> rolesModelList = rolesService.findAll();
    List<RolesDto> response = rolesMapper.toDtoList(rolesModelList);

    log.info("Roles found: {}", response);

    return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, "Roles found", response, null));
  }

  /**
   * Update a role and its permissions
   * 
   * @param rolesPermissionsPutDto - RolesPermissionsPutDto Object
   * @return ApiResponse Object
   */
  @PutMapping("/update")
  public ResponseEntity<ApiResponse<Void>> update (@Valid @RequestBody RolesPermissionsPutDto rolesPermissionsPutDto) {
    rolesService.updateRolesPermissions(rolesPermissionsPutDto);

    log.info("role updated successfully: {}", rolesPermissionsPutDto);

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ApiResponse<>(true, "Role updated successfully", null, null));
  }
}
