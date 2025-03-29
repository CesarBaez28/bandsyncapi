package com.bandsyncapi.bandsyncapi.api.v1.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bandsyncapi.bandsyncapi.api.v1.dto.roles.RolesDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.roles.RolesPermissionsDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.roles.RolesPostDto;
import com.bandsyncapi.bandsyncapi.api.v1.mappers.RolesMapper;
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
   * @param rolesRepository - Roles repository
   * @param rolesMapper     - Roles mapper
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
}
