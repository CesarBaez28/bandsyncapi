package com.bandsyncapi.bandsyncapi.api.v1.services.impl;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bandsyncapi.bandsyncapi.api.v1.dto.roles.RolesDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.roles.RolesPermissionsDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.roles.RolesPermissionsPutDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.roles.RolesPostDto;
import com.bandsyncapi.bandsyncapi.api.v1.mappers.RolesMapper;
import com.bandsyncapi.bandsyncapi.api.v1.models.PermissionsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.RolesModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.RolesPermissionsModel;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.RolesRepository;
import com.bandsyncapi.bandsyncapi.api.v1.services.RolesPermissionsService;
import com.bandsyncapi.bandsyncapi.api.v1.services.RolesService;

/**
 * Implementation of RolesService
 */
@Service
public class RolesServiceImpl implements RolesService {

  private final RolesRepository rolesRepository;

  private RolesPermissionsService rolesPermissionsService;

  private final RolesMapper rolesMapper;

  /**
   * Constructor
   * 
   * @param rolesRepository          - RolesRepository object
   * @param rolesMapper              - RolesMapper object
   * @param rolesPermissionsService  - RolesPermissionsService object
   */
  public RolesServiceImpl(RolesRepository rolesRepository, RolesMapper rolesMapper,
      RolesPermissionsService rolesPermissionsService) {
    this.rolesRepository = rolesRepository;
    this.rolesPermissionsService = rolesPermissionsService;
    this.rolesMapper = rolesMapper;
  }

  @Override
  public List<RolesModel> findAll() {
    return rolesRepository.findAll();
  }

  @Override
  @Transactional
  public RolesModel save(RolesModel rolesModel) {
    return rolesRepository.save(rolesModel);
  }

  @Override
  @Transactional
  public RolesPermissionsDto saveRoleAndPermissions(RolesPostDto rolesPostDto) {
    RolesModel rolesModel = rolesMapper.toModel(rolesPostDto);

    // Save the new role
    RolesModel roleSaved = rolesRepository.save(rolesModel);

    RolesDto rolesDto = rolesMapper.toDto(roleSaved);

    List<RolesPermissionsModel> rolesPermissions = rolesPostDto.permissions().stream()
        .map(permission -> new RolesPermissionsModel(roleSaved, permission, true)).toList();

    // Save permissions role
    List<RolesPermissionsModel> rolesPermissionsSaved = rolesPermissionsService.saveAll(rolesPermissions);

    return rolesMapper.toRolesPermissionsDto(rolesDto, rolesPermissionsSaved);
  }

  @Override
  @Transactional
  public void updateRolesPermissions(RolesPermissionsPutDto rolesPermissionsPutDto) {

    // Get the role by ID
    RolesModel rolesModel = rolesRepository.findById(rolesPermissionsPutDto.roleId())
        .orElseThrow(() -> new NoSuchElementException("Role no encontrado."));

    // Change the name of the role
    rolesModel.setName(rolesPermissionsPutDto.newName());

    // Update the role
    RolesModel updatedRole = rolesRepository.save(rolesModel);

    // Get current permissions of the role
    List<RolesPermissionsModel> actualRolesPermissions = rolesPermissionsService.findAllByRole(updatedRole);

    // Get all ids of the current permissions
    Set<Integer> actualPermissionsSet = actualRolesPermissions.stream()
        .map(permission -> permission.getPermission().getId()).collect(Collectors.toSet());

    // Get all ids of the new permissions
    Set<Integer> newPermissionsSet = rolesPermissionsPutDto.permissions().stream()
        .map(PermissionsModel::getId).collect(Collectors.toSet());

    // Get all permissions that are not presents in the new permissions list
    Set<Integer> permissionsToDelete = new HashSet<>(actualPermissionsSet);
    permissionsToDelete.removeAll(newPermissionsSet);

    // Get all permissions that are not presents in the actual permissions list
    Set<Integer> permissionsToAdd = new HashSet<>(newPermissionsSet);
    permissionsToAdd.removeAll(actualPermissionsSet);

    // Delete permissions that are not presents in the new list
    if (!permissionsToDelete.isEmpty()) {
      rolesPermissionsService.deleteByRoleIdAndPermissionIds(updatedRole.getId(), permissionsToDelete);
    }

    // Add new permissions
    if (!permissionsToAdd.isEmpty()) {
      List<RolesPermissionsModel> newPermissions = permissionsToAdd.stream()
          .map(id -> new RolesPermissionsModel(updatedRole, new PermissionsModel(id), true)).toList();
      rolesPermissionsService.saveAll(newPermissions);
    }
  }
}
