package com.bandsyncapi.bandsyncapi.api.v1.services.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.bandsyncapi.bandsyncapi.api.v1.dto.repertoires.RepertoiresPutDto;
import com.bandsyncapi.bandsyncapi.api.v1.models.RepertoiresModel;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.RepertoiresRepository;
import com.bandsyncapi.bandsyncapi.api.v1.services.RepertoiresService;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of RepertoiresService
 */
@Service
@Slf4j
public class RepertoiresServiceImpl implements RepertoiresService {

  private final RepertoiresRepository repertoiresRepository;

  /**
   * Constructor for the MusicalRolesServiceImpl class.
   * 
   * @param repertoiresRepository - Repository with methods for performing CRUD
   *                              operations on the repertoires table.
   */
  public RepertoiresServiceImpl(RepertoiresRepository repertoiresRepository) {
    this.repertoiresRepository = repertoiresRepository;
  }

  @Override
  public RepertoiresModel save(RepertoiresModel repertoiresModel) {
    log.info("Saving repertoire with name {}", repertoiresModel.getName());
    return repertoiresRepository.save(repertoiresModel);
  }

  @Override
  public List<RepertoiresModel> findByMusicalBandId(UUID musicalBandId) {
    log.info("Fetching repertoires with musical band id {}", musicalBandId);
    return repertoiresRepository.findByMusicalBandId(musicalBandId);
  }

  @Override
  public void updateRepertoire(UUID id, RepertoiresPutDto repertoiresPutDto) {
    log.info("Updating repertoire with id {}", id);

    int rowUpdated = repertoiresRepository.updateRepertoire(
        id,
        repertoiresPutDto.name(),
        repertoiresPutDto.description(),
        repertoiresPutDto.link(),
        repertoiresPutDto.status());

    if (rowUpdated == 0) {
      throw new EntityNotFoundException("Repertoire not found with id: " + id);
    }
  }

  @Override
  public void deleteById(UUID id) {
    log.info("Deleting repertoire with id {}", id);
    repertoiresRepository.deleteById(id);
  }
}
