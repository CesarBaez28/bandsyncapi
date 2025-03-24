package com.bandsyncapi.bandsyncapi.api.v1.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bandsyncapi.bandsyncapi.api.v1.models.RolesModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.RolesPermissionsModel;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.RolesPermissionsRepository;
import com.bandsyncapi.bandsyncapi.api.v1.services.RolesPermissionsService;

/**
 * This class implements the RolesPermissionsService interface
 */
@Service
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
    return rolesPermissionsRepository.findAllByRole(role);
  }
}
