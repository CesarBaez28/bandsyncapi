package com.bandsyncapi.bandsyncapi.api.v1.services.impl;

import org.springframework.stereotype.Service;

import com.bandsyncapi.bandsyncapi.api.v1.models.UsersRolesModel;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.UsersRolesRepository;
import com.bandsyncapi.bandsyncapi.api.v1.services.UsersRolesService;

import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of UsersRolesService
 */
@Service
@Slf4j
public class UsersRolesServiceImpl implements UsersRolesService {

  private final UsersRolesRepository usersRolesRepository;

  /**
   * Constructor
   * 
   * @param usersRolesRepository - UsersRolesRepository
   */
  public UsersRolesServiceImpl(UsersRolesRepository usersRolesRepository) {
    this.usersRolesRepository = usersRolesRepository;
  }

  @Override
  public UsersRolesModel save(UsersRolesModel usersRolesModel) {
    log.info("Saving user {} to role {}", usersRolesModel.getUser().getId(), usersRolesModel.getRole().getId());
    return usersRolesRepository.save(usersRolesModel);
  }
}
