package com.bandsyncapi.bandsyncapi.api.v1.services.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.bandsyncapi.bandsyncapi.api.v1.dto.repertoires.RepertoiresPutDto;
import com.bandsyncapi.bandsyncapi.api.v1.models.RepertoiresModel;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.EventsRepository;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.RepertoiresRepository;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.RepertoiresSongsRepository;
import com.bandsyncapi.bandsyncapi.api.v1.services.RepertoiresService;
import com.bandsyncapi.bandsyncapi.api.v1.services.RepertoiresSongsService;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of RepertoiresService
 */
@Service
@Slf4j
public class RepertoiresServiceImpl implements RepertoiresService {

  private final RepertoiresRepository repertoiresRepository;

  private final RepertoiresSongsRepository repertoiresSongsRepository;

  private final EventsRepository eventsRepository;

  private final RepertoiresSongsService repertoiresSongsService;

  /**
   * Constructor for the MusicalRolesServiceImpl class.
   * 
   * @param repertoiresRepository      - Repository with methods for performing
   *                                   CRUD
   *                                   operations on the repertoires table.
   * @param repertoiresSongsRepository - Repository with methods for performing
   *                                   CRUD
   *                                   operations on the repertoires_songs table.
   * @param eventsRepository           - Repository with methods for performing
   *                                   CRUD
   *                                   operations on the events table.
   */
  public RepertoiresServiceImpl(RepertoiresRepository repertoiresRepository,
      RepertoiresSongsRepository repertoiresSongsRepository, RepertoiresSongsService repertoiresSongsService,
      EventsRepository eventsRepository) {
    this.eventsRepository = eventsRepository;
    this.repertoiresSongsRepository = repertoiresSongsRepository;
    this.repertoiresRepository = repertoiresRepository;
    this.repertoiresSongsService = repertoiresSongsService;
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

    log.info("Repertoire with id {} updated successfully", id); 
    repertoiresSongsService.updateRepertoireSongs(id, repertoiresPutDto.songs());

    log.info("Repertoire songs with id {} updated successfully", id);
  }

  @Override
  public void deleteById(UUID id) {
    log.info("Deleting repertoire with id {}", id);

    log.info("Deleting events associated with repertoire id {}", id);
    eventsRepository.deleteByRepertoireId(id);

    log.info("Deleting repertoire songs associated with repertoire id {}", id);
    repertoiresSongsRepository.deleteByRepertoireId(id);

    repertoiresRepository.deleteById(id);
  }

  @Override
  public Page<RepertoiresModel> find(UUID musicalBandId, String term, int page, int size) {
    log.info("Finding repertoires by musical band id: {} and term: {}", musicalBandId, term);
    return repertoiresRepository.find(musicalBandId, term, PageRequest.of(page, size, Sort.by("name")));
  }

  @Override
  public RepertoiresModel findById(UUID id) {
    return repertoiresRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Repertoire not found with id: " + id));
  }
}
