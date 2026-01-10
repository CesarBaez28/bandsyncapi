package com.bandsyncapi.bandsyncapi.api.v1.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bandsyncapi.bandsyncapi.api.v1.constants.UserPermissions;
import com.bandsyncapi.bandsyncapi.api.v1.dto.roles.RoleAndPermissionsDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.roles.RolesDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.roles.RolesPermissionsDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.roles.RolesPermissionsPutDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.roles.RolesPostDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.roles.UserRolesAndPermissionsDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.roles.UserRoleDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.users.UsersDto;
import com.bandsyncapi.bandsyncapi.api.v1.mappers.RolesMapper;
import com.bandsyncapi.bandsyncapi.api.v1.mappers.UsersMapper;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalBandsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.RolesModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.UsersModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.UsersRolesModel;
import com.bandsyncapi.bandsyncapi.api.v1.services.RolesPermissionsService;
import com.bandsyncapi.bandsyncapi.api.v1.services.RolesService;
import com.bandsyncapi.bandsyncapi.api.v1.services.UsersRolesService;
import com.bandsyncapi.bandsyncapi.response.ApiResponse;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

  private final RolesPermissionsService rolesPermissionsService;

  private final UsersRolesService usersRolesService;

  private final RolesMapper rolesMapper;

  private final UsersMapper usersMapper;

  /**
   * Constructor
   * 
   * @param rolesService            - Roles service
   * @param rolesPermissionsService - RolesPermissions service
   * @param usersRolesService       - UsersRoles service
   * @param rolesMapper             - Roles mapper to convert between RolesModel
   *                                and RolesDto.
   * @param usersMapper             - users mapper to convert between UserModel
   *                                and
   *                                UserDto.
   */
  public RolesController(RolesService rolesService, RolesPermissionsService rolesPermissionsService,
      UsersRolesService usersRolesService, RolesMapper rolesMapper, UsersMapper usersMapper) {
    this.rolesService = rolesService;
    this.rolesPermissionsService = rolesPermissionsService;
    this.usersRolesService = usersRolesService;
    this.rolesMapper = rolesMapper;
    this.usersMapper = usersMapper;
  }

  /**
   * Save a new role
   * 
   * 
   * @param rolesPostDto - RolesPostDto Object
   * @return RolesPermissionsDto object
   */
  @PostMapping("/save")
  @PreAuthorize("hasRole('" + UserPermissions.ADD_ROLE + "')")
  public ResponseEntity<ApiResponse<RolesPermissionsDto>> save(@Valid @RequestBody RolesPostDto rolesPostDto) {

    RolesPermissionsDto response = rolesService.saveRoleAndPermissions(rolesPostDto);

    log.info("Role saved successfully");

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new ApiResponse<>(true, "Role saved successfully.", response, null));
  }

  /**
   * Assign a role to a user in a musical band
   * 
   * @param roleId        - role id
   * @param userId        - user id
   * @param musicalBandId - musical band id
   * @return ApiResponse indicating the role was assigned
   */
  @PostMapping("/assign/role/{roleId}/user/{userId}/musicalBand/{musicalBandId}")
  @PreAuthorize("hasRole('" + UserPermissions.ASSIGN_ROLE + "')")
  public ResponseEntity<ApiResponse<Void>> assignRoleToUserInBand(@PathVariable Integer roleId,
      @PathVariable UUID userId, @PathVariable UUID musicalBandId) {
    usersRolesService.assignRoleToUserInBand(roleId, userId, musicalBandId);

    log.info("Role assigned to user successfully");

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new ApiResponse<>(true, "Role assigned to user successfully.", null, null));
  }

  /**
   * Update user role in a musical band
   * 
   * @param userId        - user id
   * @param musicalBandId - musical band id
   * @param newRole       - new role
   * @return - ApiResponse indicating the user role was updated
   */
  @PutMapping("/user/{userId}/musicalBand/{musicalBandId}")
  @PreAuthorize("hasRole('" + UserPermissions.UPDATE_ROLE + "')")
  public ResponseEntity<ApiResponse<Void>> updateUserRole(@PathVariable UUID userId, @PathVariable UUID musicalBandId,
      @RequestBody RolesModel newRole) {

    usersRolesService.updateUserRole(newRole, userId, musicalBandId);

    log.info("User role updated successfully");

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ApiResponse<>(true, "User role uptaded successfully", null, null));
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
   * Find all roles permissions by musical band id
   * 
   * @param musicalBandId - musical band id
   * @return - A ApiResponse Object with the roles permissions
   */
  @GetMapping("/findByMusicalBandId/{musicalBandId}")
  @PreAuthorize("hasRole('" + UserPermissions.VIEW_ROLES_AND_PERMISSIONS + "')")
  public ResponseEntity<ApiResponse<List<RoleAndPermissionsDto>>> findByMusicalBandId(
      @PathVariable("musicalBandId") UUID musicalBandId) {
    List<RoleAndPermissionsDto> rolesPermissions = rolesPermissionsService.findByMusicalBandId(musicalBandId);

    log.info("Roles permissions found for musical band id {}: ", musicalBandId);

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ApiResponse<>(true, "Roles permissions found", rolesPermissions, null));
  }

  /**
   * find roles and permissions of ther user in a musical band
   * 
   * @param userId        - user id
   * @param musicalBandId - musical band id
   * @return Role and permissions of ther user in the musical band
   */
  @GetMapping("/findByUserIdAndMusicalBandId/{userId}/{musicalBandId}")
  @PreAuthorize("hasRole('" + UserPermissions.VIEW_ROLES_AND_PERMISSIONS + "')")
  public ResponseEntity<ApiResponse<RoleAndPermissionsDto>> findByUserIdAndMusicalBandId(@PathVariable UUID userId,
      @PathVariable UUID musicalBandId) {
    RoleAndPermissionsDto userRoles = usersRolesService.findByUserIdAndMusicalBandId(userId, musicalBandId);

    log.info("Role and permissions of the user in musicalBand id {} found successfully", musicalBandId);

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ApiResponse<>(true, "Role and permissions of the user in musicalBand found successfully", userRoles,
            null));
  }

  /**
   * Find all of a user's roles in the different bands they belong to
   * 
   * @param userId - user id
   * @return Role and permissions of ther user
   */
  @GetMapping("/user/{userId}")
  public ResponseEntity<ApiResponse<List<UserRolesAndPermissionsDto>>> findByUserId(@PathVariable UUID userId) {
    List<UserRolesAndPermissionsDto> userRoles = usersRolesService.findByUser(new UsersModel(userId));

    log.info("Role and permissions of the user {} found successfully", userId);

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ApiResponse<>(true, "Role and permissions of the user found successfully", userRoles, null));
  }

  /**
   * find all user'roles in a musical band
   * 
   * @param musicalBandId - musical band id
   * @return - List of users and his roles in the musical band
   */
  @GetMapping("/users/musicalBand/{musicalBandId}")
  @PreAuthorize("hasRole('" + UserPermissions.VIEW_ROLES_AND_PERMISSIONS + "')")
  public ResponseEntity<ApiResponse<List<UserRoleDto>>> findUsersRolesByMusicalBandId(
      @PathVariable UUID musicalBandId) {
    List<UserRoleDto> usersRoles = usersRolesService.findByMusicalBand(new MusicalBandsModel(musicalBandId));

    log.info("Users and his roles of the musical band {} found successfully", usersRoles);

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ApiResponse<>(true, "Users and his roles of the musical band found successfully", usersRoles, null));
  }

  /**
   * Update a role and its permissions
   * 
   * @param rolesPermissionsPutDto - RolesPermissionsPutDto Object
   * @return ApiResponse Object
   */
  @PutMapping("/update")
  @PreAuthorize("hasRole('" + UserPermissions.UPDATE_ROLE + "')")
  public ResponseEntity<ApiResponse<Void>> update(@Valid @RequestBody RolesPermissionsPutDto rolesPermissionsPutDto) {
    rolesService.updateRolesPermissions(rolesPermissionsPutDto);

    log.info("role updated successfully");

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ApiResponse<>(true, "Role updated successfully", null, null));
  }

  /**
   * Delete a role by if there are no users with that id
   * 
   * @param roleId - role id
   * @return A list of users if there are users with that role or empty list if
   *         not.
   */
  @DeleteMapping("/delete/{roleId}")
  @PreAuthorize("hasRole('" + UserPermissions.DELETE_ROLE + "')")
  public ResponseEntity<ApiResponse<List<UsersDto>>> delete(@PathVariable Integer roleId) {
    List<UsersRolesModel> usersRole = usersRolesService.findByRoleId(roleId);

    if (!usersRole.isEmpty()) {
      List<UsersModel> usersModel = usersRole.stream().map(u -> u.getUser()).toList();
      List<UsersDto> users = usersMapper.toDtoList(usersModel);

      return ResponseEntity.status(HttpStatus.OK)
          .body(new ApiResponse<>(false, "It cannot be deleted because there are users with that role.", users, null));
    }

    rolesService.deleteRoleById(roleId);
    return ResponseEntity.status(HttpStatus.OK)
        .body(new ApiResponse<>(true, "Role deleted successfully.", List.of(), null));
  }

  /**
   * Delete a user role from a musical band
   * 
   * @param userId        - user id
   * @param musicalBandId - musical band id
   * @return - ApiResponse indicating the user role was deleted from the musical
   *         band
   */
  @DeleteMapping("/delete/user/{userId}/musicalBand/{musicalBandId}")
  @PreAuthorize("hasRole('" + UserPermissions.DELETE_ROLE + "')")
  public ResponseEntity<ApiResponse<Void>> deleteUserRoleFromMusicalBand(@PathVariable UUID userId,
      @PathVariable UUID musicalBandId) {
    usersRolesService.deleteByUserIdAndMusicalBandId(userId, musicalBandId);

    log.info("User {} deleted from musical band {}", userId, musicalBandId);

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ApiResponse<>(true, "User deleted from musical band successfully", null, null));
  }
}
