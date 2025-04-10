package com.bandsyncapi.bandsyncapi.api.v1.services.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalRolesModel;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.MusicalRolesRepository;
import com.bandsyncapi.bandsyncapi.api.v1.services.MusicalRolesService;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

/**
 * This class is a service implementation of the MusicalGenresService interface.
 */
@Service
@Slf4j
public class MusicalRolesServiceImpl implements MusicalRolesService {

  private final MusicalRolesRepository musicalRolesRepository;

  /**
   * Constructor for the MusicalRolesServiceImpl class.
   * @param musicalRolesRepository - Repository with methods for performing CRUD operations on the musical_roles table.
   */
  public MusicalRolesServiceImpl (MusicalRolesRepository musicalRolesRepository) {
    this.musicalRolesRepository = musicalRolesRepository;
  }

  @Override
  public MusicalRolesModel save(MusicalRolesModel musicalRolesModel) {
    log.info("Saving musical role: {}", musicalRolesModel);
    return musicalRolesRepository.save(musicalRolesModel);
  }

  @Override
  public List<MusicalRolesModel> findByMusicalBandId(UUID musicalBandId) {
    log.info("Finding musical roles by musical band id: {}", musicalBandId);
    return musicalRolesRepository.findByMusicalBandId(musicalBandId);
  }

  @Override
  public void updateMusicalRoleName(Integer id, String name) {
    log.info("Updating musical role name with id: {} to {}", id, name);
    int rowsUpdated = musicalRolesRepository.updateMusicalRoleName(id, name);

    if (rowsUpdated == 0) {
      throw new EntityNotFoundException("No se encontró un role musical con ese id");
    }
  }

  @Override
  public void deleteById(Integer id) {
    log.info("Deleting musical role with id: {}", id);  
    musicalRolesRepository.deleteById(id);
  }

}
