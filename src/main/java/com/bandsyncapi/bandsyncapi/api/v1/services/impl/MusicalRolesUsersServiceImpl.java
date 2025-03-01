package com.bandsyncapi.bandsyncapi.api.v1.services.impl;

import java.util.List;
import java.util.UUID;

import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;

import com.bandsyncapi.bandsyncapi.api.v1.projections.MusicalRolesSingleUserProjection;
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

    List<MusicalRolesUsersProjection> projections = musicalRolesUsersRepository.findAllByMusicalBandId(musicalBandId);

    if (projections.isEmpty()) {
      throw new NoSuchElementException("No se encontraron datos");
    }

    return projections;
  }

  @Override
  public List<MusicalRolesSingleUserProjection> findMusicalRolesUser(UUID musicalBandId, UUID userId) {
    List<MusicalRolesSingleUserProjection> musicalRoles = musicalRolesUsersRepository.findMusicalRolesUser(musicalBandId, userId);

    if (musicalRoles.isEmpty()) {
      throw new NoSuchElementException("No se encontraron datoa");
    }

    return musicalRoles;
  }
}
