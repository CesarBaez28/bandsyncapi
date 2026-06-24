package com.bandsyncapi.bandsyncapi.api.v1.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.bandsyncapi.bandsyncapi.api.v1.dto.permissions.PermissionDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.roles.RoleAndPermissionsDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.roles.RolesDto;
import com.bandsyncapi.bandsyncapi.api.v1.mappers.PermissionsMapper;
import com.bandsyncapi.bandsyncapi.api.v1.mappers.RolesMapper;
import com.bandsyncapi.bandsyncapi.api.v1.models.RolesModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.RolesPermissionsModel;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.RolesPermissionsRepository;
import com.bandsyncapi.bandsyncapi.api.v1.services.RolesPermissionsService;

import lombok.extern.slf4j.Slf4j;

/**
 * This class implements the RolesPermissionsService interface
 */
@Service
@Slf4j
public class RolesPermissionsServiceImpl implements RolesPermissionsService {

  private final RolesPermissionsRepository rolesPermissionsRepository;

  private final RolesMapper rolesMapper;

  private final PermissionsMapper permissionsMapper;

  /**
   * Constructor
   * 
   * @param rolesPermissionsRepository - RolesPermissionsRepository object
   * @param rolesMapper                - RolesMapper
   * @param permissionsMapper          - PermissionsMapper
   */
  public RolesPermissionsServiceImpl(RolesPermissionsRepository rolesPermissionsRepository, RolesMapper rolesMapper, PermissionsMapper permissionsMapper) {
    this.rolesPermissionsRepository = rolesPermissionsRepository;
    this.rolesMapper = rolesMapper;
    this.permissionsMapper = permissionsMapper;
  }

  @Override
  public List<RolesPermissionsModel> findAllByRole(RolesModel role) {
    log.info("Finding all permissions for role {}", role.getId());
    return rolesPermissionsRepository.findAllByRole(role.getId());
  }

  @Override
  public List<RoleAndPermissionsDto> findByMusicalBandId(UUID musicalBandId) {
    log.info("Finding all roles permissions for musical band id {}", musicalBandId);

    List<RolesPermissionsModel> rolesPermissions = rolesPermissionsRepository.findByMusicalBandId(musicalBandId);

    List<RolesModel> roles = rolesPermissions.stream()
        .map(RolesPermissionsModel::getRole)
        .distinct()
        .toList();

    List<RoleAndPermissionsDto> roleAndPermissionsDtos = new ArrayList<>();

    for (RolesModel role : roles) {
      List<PermissionDto> permissions = rolesPermissions.stream()
          .filter(rp -> rp.getRole().getId().equals(role.getId()))
          .map(RolesPermissionsModel::getPermission)
          .map(permissionsMapper::toDto)
          .toList();

      RolesDto roleDto = rolesMapper.toDto(role);

      roleAndPermissionsDtos.add(new RoleAndPermissionsDto(roleDto, permissions));
    }

    return roleAndPermissionsDtos;
  }

  @Override
  public RolesPermissionsModel save(RolesPermissionsModel rolesPermissionsModel) {
    log.info("Saving roles permissions {}", rolesPermissionsModel);
    return rolesPermissionsRepository.save(rolesPermissionsModel);
  }

  @Override
  public List<RolesPermissionsModel> saveAll(List<RolesPermissionsModel> rolesPermissionsModel) {
    log.info("Saving all roles permissions");
    return rolesPermissionsRepository.saveAll(rolesPermissionsModel);
  }

  @Override
  public void deleteByRoleIdAndPermissionIds(Integer id, Set<Integer> permissionsToDelete) {
    log.info("Deleting permissions {} for role {}", permissionsToDelete, id);
    rolesPermissionsRepository.deleteByRoleIdAndPermissionIdIn(id, permissionsToDelete);
  }

  @Override
  public void deleteByRoleId(Integer roleId) {
    log.info("Deleting role and permissions by role id: {}", roleId);
    rolesPermissionsRepository.deleteByRoleId(roleId);
  }

  @Override
  public void deleteByRoleIdIn(Set<Integer> roleIds) {
    log.info("Deleting a list of role using ids: {}", roleIds);
    rolesPermissionsRepository.deleteByRoleIdIn(roleIds);
  }
}
