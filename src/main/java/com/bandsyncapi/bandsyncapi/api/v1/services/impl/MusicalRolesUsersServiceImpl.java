package com.bandsyncapi.bandsyncapi.api.v1.services.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.bandsyncapi.bandsyncapi.api.v1.projections.MusicalRolesUsersProjection;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.MusicalRolesUsersRepository;
import com.bandsyncapi.bandsyncapi.api.v1.services.MusicalRolesUsersService;

/**
 * Implementation of MusicalRolesUsersService
 */
@Service
public class MusicalRolesUsersServiceImpl implements MusicalRolesUsersService{

  private final MusicalRolesUsersRepository musicalRolesUsersRepository;

  /**
   * Constructor
   * 
   * @param musicalRolesUsersRepository - MusicalRolesUsers repository 
   */
  public MusicalRolesUsersServiceImpl (MusicalRolesUsersRepository musicalRolesUsersRepository) {
    this.musicalRolesUsersRepository = musicalRolesUsersRepository;
  }

  @Override
  public List<MusicalRolesUsersProjection> findAllByMusicalBandId(UUID musicalBandId) {
    return musicalRolesUsersRepository.findAllByMusicalBandId(musicalBandId);
  }
}
