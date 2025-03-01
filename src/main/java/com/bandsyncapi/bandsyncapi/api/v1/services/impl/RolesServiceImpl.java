package com.bandsyncapi.bandsyncapi.api.v1.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bandsyncapi.bandsyncapi.api.v1.models.RolesModel;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.RolesRepository;
import com.bandsyncapi.bandsyncapi.api.v1.services.RolesService;

/**
 * Implementation of RolesService
 */
@Service
public class RolesServiceImpl implements RolesService{

  private final RolesRepository rolesRepository;

  /**
   * Constructor
   * 
   * @param rolesRepository - Roles repository
   */
  public RolesServiceImpl (RolesRepository rolesRepository) {
    this.rolesRepository = rolesRepository;
  }

  @Override
  public List<RolesModel> findAll() {
    return rolesRepository.findAll();
  }
}
