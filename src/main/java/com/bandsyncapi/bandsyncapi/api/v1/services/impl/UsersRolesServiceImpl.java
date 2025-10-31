package com.bandsyncapi.bandsyncapi.api.v1.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.bandsyncapi.bandsyncapi.api.v1.dto.musicalbands.MusicalBandsDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.permissions.PermissionDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.roles.RoleAndPermissionsDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.roles.RolesDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.roles.UserRolesAndPermissionsDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.roles.UserRoleDto;
import com.bandsyncapi.bandsyncapi.api.v1.mappers.MusicalBandsMapper;
import com.bandsyncapi.bandsyncapi.api.v1.mappers.PermissionsMapper;
import com.bandsyncapi.bandsyncapi.api.v1.mappers.RolesMapper;
import com.bandsyncapi.bandsyncapi.api.v1.mappers.UsersRolesMapper;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalBandsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.RolesModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.RolesPermissionsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.UsersModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.UsersRolesModel;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.RolesPermissionsRepository;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.UsersRolesRepository;
import com.bandsyncapi.bandsyncapi.api.v1.services.UsersRolesService;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of UsersRolesService
 */
@Service
@Slf4j
public class UsersRolesServiceImpl implements UsersRolesService {

  private final UsersRolesRepository usersRolesRepository;

  private final RolesPermissionsRepository rolesPermissionsRepository;

  private final UsersRolesMapper usersRolesMapper;

  private final MusicalBandsMapper musicalBandsMapper;

  private final PermissionsMapper permissionsMapper;

  private final RolesMapper rolesMapper;

  /**
   * Constructor
   * 
   * @param usersRolesRepository       - UsersRolesRepository
   * @param rolesPermissionsRepository - RolesPermissionsRepository
   * @param usersRolesMapper           - UsersRolesMapper
   * @param musicalBandsMapper         - MusicalBandsMapper
   * @param permissionsMapper          - Permissions mapper
   * @param rolesMapper                - Roles mapper
   */
  public UsersRolesServiceImpl(UsersRolesRepository usersRolesRepository,
      RolesPermissionsRepository rolesPermissionsRepository, UsersRolesMapper usersRolesMapper,
      MusicalBandsMapper musicalBandsMapper,
      PermissionsMapper permissionsMapper,
      RolesMapper rolesMapper) {
    this.usersRolesRepository = usersRolesRepository;
    this.rolesPermissionsRepository = rolesPermissionsRepository;
    this.usersRolesMapper = usersRolesMapper;
    this.musicalBandsMapper = musicalBandsMapper;
    this.permissionsMapper = permissionsMapper;
    this.rolesMapper = rolesMapper;
  }

  @Override
  public UsersRolesModel save(UsersRolesModel usersRolesModel) {
    log.info("Saving user {} to role {}", usersRolesModel.getUser().getId(), usersRolesModel.getRole().getId());
    return usersRolesRepository.save(usersRolesModel);
  }

  @Override
  public void updateUserRole(RolesModel role, UUID userId, UUID musicalBandId) {
    log.info("Updating user {} in musicalband {} with role {}", userId, musicalBandId, role.getId());
    
    int row = usersRolesRepository.updateUserRolec(role, userId, musicalBandId);

    if (row == 0) {
      log.error("user role not found with userId: {} and musicalBandId: {}", userId, musicalBandId);
      throw new EntityNotFoundException("user role not found");
    }
  }

  @Override
  public List<UsersRolesModel> findByRoleId(Integer roleId) {
    log.info("Finding users with role id: {} ", roleId);
    return usersRolesRepository.findByRole(new RolesModel(roleId));
  }

  @Override
  public RoleAndPermissionsDto findByUserIdAndMusicalBandId(UUID userId, UUID musicalBandId) {
    log.info("Finding role of the user in a musical band");

    UsersRolesModel usersRolesModel = usersRolesRepository.findByUserIdAndMusicalBandId(userId, musicalBandId)
        .orElseThrow(() -> new EntityNotFoundException(
            "Role not found by user " + userId + " and musicalBand " + musicalBandId));

    List<RolesPermissionsModel> rolesPermissionsModel = rolesPermissionsRepository
        .findAllByRole(usersRolesModel.getRole());

    List<PermissionDto> permissions = rolesPermissionsModel.stream()
        .map(RolesPermissionsModel::getPermission)
        .map(permissionsMapper::toDto)
        .toList();

    RolesDto roleDto = rolesMapper.toDto(usersRolesModel.getRole());

    return new RoleAndPermissionsDto(roleDto, permissions);
  }

  @Override
  public List<UserRolesAndPermissionsDto> findByUser(UsersModel user) {
    log.info("Finding all of a user's roles in the different bands they belong to ");

    List<UsersRolesModel> userRolesModel = usersRolesRepository.findByUser(user);

    List<RolesModel> roles = userRolesModel.stream()
        .map(UsersRolesModel::getRole)
        .distinct()
        .toList();

    List<RolesPermissionsModel> rolesPermissions = rolesPermissionsRepository.findAllByRoleIn(roles);

    List<UserRolesAndPermissionsDto> userRolesAndPermissions = new ArrayList<>();
    for (RolesModel role : roles) {
      List<PermissionDto> permissions = rolesPermissions.stream()
          .filter(rp -> rp.getRole().getId().equals(role.getId()))
          .map(RolesPermissionsModel::getPermission)
          .map(permissionsMapper::toDto)
          .toList();

      MusicalBandsDto musicalBandsDto = musicalBandsMapper.toDto(role.getMusicalBand());
      RolesDto rolesDto = rolesMapper.toDto(role);
      userRolesAndPermissions.add(new UserRolesAndPermissionsDto(musicalBandsDto, rolesDto, permissions));
    }

    return userRolesAndPermissions;
  }

  @Override
  public List<UserRoleDto> findByMusicalBand(MusicalBandsModel musicalBand) {
    log.info("Finding all users roles in the musical band: {} ", musicalBand.getId());

    List<UsersRolesModel> usersRolesModel = usersRolesRepository.findByMusicalBand(musicalBand);

    return usersRolesMapper.toDtoList(usersRolesModel);
  }
}
