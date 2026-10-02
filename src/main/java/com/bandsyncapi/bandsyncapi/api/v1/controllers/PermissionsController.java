package com.bandsyncapi.bandsyncapi.api.v1.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bandsyncapi.bandsyncapi.api.v1.constants.UserPermissions;
import com.bandsyncapi.bandsyncapi.api.v1.models.PermissionsModel;
import com.bandsyncapi.bandsyncapi.api.v1.services.PermissionsService;
import com.bandsyncapi.bandsyncapi.constants.Constants;
import com.bandsyncapi.bandsyncapi.response.ApiResponse;
import com.bandsyncapi.bandsyncapi.security.RateLimited;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller to handle request for permissions data
 */
@RestController
@RequestMapping(path = "api/v1/permissions")
@Slf4j
@RateLimited(capacity = Constants.RATE_LIMIT_CAPACITY, refillTokens = Constants.RATE_LIMIT_TOKENS, refillMinutes = Constants.RATE_LIMIT_MINUTES)
public class PermissionsController {

  private final PermissionsService permissionsService;

  /**
   * Constructor
   * 
   * @param permissionsService - Permissions service
   */
  public PermissionsController(PermissionsService permissionsService) {
    this.permissionsService = permissionsService;
  }

  /**
   * Get all permissions
   * 
   * @return - A ApiResponse Object with the list of permissions
   */
  @GetMapping("/findAll")
  @PreAuthorize("hasRole('" + UserPermissions.VIEW_ROLES_AND_PERMISSIONS + "')")
  public ResponseEntity<ApiResponse<List<PermissionsModel>>> findAll() {
    List<PermissionsModel> permissions = permissionsService.findAll();

    log.info("Permissions found");

    return ResponseEntity.ok(new ApiResponse<>(true, "Permissions fetched successfully", permissions, null));
  }
}