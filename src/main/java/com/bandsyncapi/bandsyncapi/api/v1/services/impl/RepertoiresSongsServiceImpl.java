package com.bandsyncapi.bandsyncapi.api.v1.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.bandsyncapi.bandsyncapi.api.v1.models.RepertoiresModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.RepertoiresSongsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.SongsModel;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.RepertoiresSongsRepository;
import com.bandsyncapi.bandsyncapi.api.v1.services.RepertoiresSongsService;

import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of RepertoiresSongsService
 * 
 */
@Service
@Slf4j
public class RepertoiresSongsServiceImpl implements RepertoiresSongsService{

  private final RepertoiresSongsRepository repertoiresSongsRepository;

  /**
   * Constructor of the class
   * 
   * @param repertoiresSongsRepository - Repository to perform crud operations on RepertoiresSongsModel
   */
  public RepertoiresSongsServiceImpl (RepertoiresSongsRepository repertoiresSongsRepository) {
    this.repertoiresSongsRepository = repertoiresSongsRepository;
  }

  @Override
  public List<RepertoiresSongsModel> saveAll(RepertoiresModel repertoiresModel, List<SongsModel> songsModels) {
    log.info("Saving all songs in repertoire {}", repertoiresModel.getId());

    List<RepertoiresSongsModel> repertoiresSongsModels = new ArrayList<>();

    for (SongsModel song : songsModels) {
      repertoiresSongsModels.add(new RepertoiresSongsModel(repertoiresModel, song, true));
    }

    return repertoiresSongsRepository.saveAll(repertoiresSongsModels);
  }  
}
