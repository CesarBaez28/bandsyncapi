package com.bandsyncapi.bandsyncapi.api.v1.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bandsyncapi.bandsyncapi.api.v1.dto.permissions.TypeOfPermissionDto;
import com.bandsyncapi.bandsyncapi.api.v1.mappers.TypeOfPermissionsMapper;
import com.bandsyncapi.bandsyncapi.api.v1.models.TypeOfPermissionsModel;
import com.bandsyncapi.bandsyncapi.api.v1.services.TypeOfPermissionsService;
import com.bandsyncapi.bandsyncapi.response.ApiResponse;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller to handle request for type of permissions data
 */
@RestController
@RequestMapping(path = "api/v1/type-of-permissions")
@Slf4j
public class TypeOfPermissionsController {

  private final TypeOfPermissionsService typeOfPermissionsService;

  private final TypeOfPermissionsMapper typeOfPermissionsMapper;

  /**
   * Constructor
   * 
   * @param typeOfPermissionsService - TypeOfPermissions service
   * @param typeOfPermissionsMapper  - TypeOfPermissions mapper
   */
  public TypeOfPermissionsController(TypeOfPermissionsService typeOfPermissionsService,
      TypeOfPermissionsMapper typeOfPermissionsMapper) {
    this.typeOfPermissionsService = typeOfPermissionsService;
    this.typeOfPermissionsMapper = typeOfPermissionsMapper;
  }

  /**
   * Get all type of permissions
   * 
   * @return - A ApiResponse Object with the list of type of permissions
   */
  @GetMapping("/findAll")
  public ResponseEntity<ApiResponse<List<TypeOfPermissionDto>>> findAll() {
    List<TypeOfPermissionsModel> typeOfPermissions = typeOfPermissionsService.findAll();
    List<TypeOfPermissionDto> typeOfPermissionsDto = typeOfPermissionsMapper.toDtoList(typeOfPermissions);

    log.info("Type of permissions found");

    return ResponseEntity
        .ok(new ApiResponse<>(true, "Type of permissions fetched successfully", typeOfPermissionsDto, null));
  }

}
