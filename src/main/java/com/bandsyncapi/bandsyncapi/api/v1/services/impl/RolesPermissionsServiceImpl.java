package com.bandsyncapi.bandsyncapi.api.v1.services.impl;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

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

  /**
   * Constructor
   * 
   * @param rolesPermissionsRepository - RolesPermissionsRepository object
   */
  public RolesPermissionsServiceImpl(RolesPermissionsRepository rolesPermissionsRepository) {
    this.rolesPermissionsRepository = rolesPermissionsRepository;
  }

  @Override
  public List<RolesPermissionsModel> findAllByRole(RolesModel role) {
    log.info("Finding all permissions for role {}", role.getId());
    return rolesPermissionsRepository.findAllByRole(role);
  }

  @Override
  public RolesPermissionsModel save(RolesPermissionsModel rolesPermissionsModel) {
    log.info("Saving roles permissions {}", rolesPermissionsModel);
    return rolesPermissionsRepository.save(rolesPermissionsModel);
  }

  @Override
  public List<RolesPermissionsModel> saveAll(List<RolesPermissionsModel> rolesPermissionsModel) {
    log.info("Saving all roles permissions {}", rolesPermissionsModel);
    return rolesPermissionsRepository.saveAll(rolesPermissionsModel);
  }

  @Override
  public void deleteByRoleIdAndPermissionIds(Integer id, Set<Integer> permissionsToDelete) {
    log.info("Deleting permissions {} for role {}", permissionsToDelete, id);
    rolesPermissionsRepository.deleteByRoleIdAndPermissionIdIn(id, permissionsToDelete);
  }
}
