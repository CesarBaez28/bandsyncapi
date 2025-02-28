package com.bandsyncapi.bandsyncapi.api.v1.services.impl;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.bandsyncapi.bandsyncapi.api.v1.models.UsersModel;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.UsersRepository;
import com.bandsyncapi.bandsyncapi.api.v1.services.UsersService;

/**
 * Implementation of UsersService
 */
@Service
public class UsersServiceImpl implements UsersService{

  private final UsersRepository usersRepository;

  /**
   * Constructor
   * 
   * @param usersRepository - Users Repository 
   */
  public UsersServiceImpl (UsersRepository usersRepository) {
    this.usersRepository = usersRepository;
  }

  @Override
  public List<UsersModel> getAllUsersByMusicalBandId(UUID musicalBandId) {

    List<UsersModel> users = usersRepository.findAllByMusicalBandId(musicalBandId);

    if (users.isEmpty()) {
      throw new NoSuchElementException("No se encontraron datos");
    }

    return users;
  }  
}
