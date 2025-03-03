package com.bandsyncapi.bandsyncapi.api.v1.services.impl;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.NoSuchElementException;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalBandsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalRolesModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalRolesUsersModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.UsersModel;
import com.bandsyncapi.bandsyncapi.api.v1.projections.MusicalRolesSingleUserProjection;
import com.bandsyncapi.bandsyncapi.api.v1.projections.MusicalRolesUsersProjection;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.MusicalRolesUsersRepository;
import com.bandsyncapi.bandsyncapi.api.v1.services.MusicalRolesUsersService;

/**
 * Implementation of MusicalRolesUsersService
 */
@Service
public class MusicalRolesUsersServiceImpl implements MusicalRolesUsersService {

  private final MusicalRolesUsersRepository musicalRolesUsersRepository;

  /**
   * Constructor
   * 
   * @param musicalRolesUsersRepository - MusicalRolesUsers repository
   */
  public MusicalRolesUsersServiceImpl(MusicalRolesUsersRepository musicalRolesUsersRepository) {
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
    return musicalRolesUsersRepository.findMusicalRolesUser(musicalBandId, userId);
  }

  @Override
  public void assignMusicalRolesUser(UUID userId, UUID musicalBandId, List<MusicalRolesModel> musicalRoles) {
    List<MusicalRolesSingleUserProjection> actualRoles = findMusicalRolesUser(musicalBandId, userId);

    Set<Integer> actualRolesSet = actualRoles.stream().map(MusicalRolesSingleUserProjection::getId)
        .collect(Collectors.toSet());

    Set<Integer> newRolesSet = musicalRoles.stream().map(role -> role.getId()).collect(Collectors.toSet());

    // Get all roles that are no presents in the new roles list
    Set<Integer> rolesToDelete = new HashSet<>(actualRolesSet);
    rolesToDelete.removeAll(newRolesSet);

    // Get all roles that are not presents in the actual roles list
    Set<Integer> rolesToAdd = new HashSet<>(newRolesSet);
    rolesToAdd.removeAll(actualRolesSet);

    // Deletes roles that are not presents in the new list
    if (!rolesToDelete.isEmpty()) {
      musicalRolesUsersRepository.deleteByUserIdAndBandIdAndRoleIds(userId, musicalBandId, rolesToDelete);
    }

    // Add new roles
    if (!rolesToAdd.isEmpty()) {
      List<MusicalRolesUsersModel> newRoles = rolesToAdd.stream()
          .map(id -> new MusicalRolesUsersModel(new MusicalRolesModel(id), new MusicalBandsModel(musicalBandId),
              new UsersModel(userId), true))
          .toList();
      musicalRolesUsersRepository.saveAll(newRoles);
    }
  }
}
