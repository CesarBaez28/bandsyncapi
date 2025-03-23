package com.bandsyncapi.bandsyncapi.api.v1.services.impl;

import org.springframework.stereotype.Service;

import com.bandsyncapi.bandsyncapi.api.v1.models.UsersRolesModel;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.UsersRolesRepository;
import com.bandsyncapi.bandsyncapi.api.v1.services.UsersRolesService;

/**
 * Implementation of UsersRolesService
 */
@Service
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
    return usersRolesRepository.save(usersRolesModel);
  }
}
