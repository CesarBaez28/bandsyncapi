package com.bandsyncapi.bandsyncapi.api.v1.services;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;

import com.bandsyncapi.bandsyncapi.api.v1.dto.repertoires.RepertoiresPutDto;
import com.bandsyncapi.bandsyncapi.api.v1.models.RepertoiresModel;

public interface RepertoiresService {

  /**
   * Save a new repertoire
   * 
   * @param repertoiresModel - repertoire object to be save
   * @return - the repertoire saved
   */
  public RepertoiresModel save(RepertoiresModel repertoiresModel);

  /**
   * Finds repertoires by musicalBandId
   * 
   * @param musicalBandId - musical band id
   * @return - A repertoires list
   */
  public List<RepertoiresModel> findByMusicalBandId(UUID musicalBandId);

  /**
   * Finds repertoires by musical band id, name and description
   * 
   * @param musicalBandId - musical band id
   * @param term          - search term
   * @param page          - page number
   * @param size          - page size
   * @return - Page of RepertoiresModel
   */
  public Page<RepertoiresModel> find(UUID musicalBandId, String term, int page, int size);

  /**
   * finds a repertoire by id
   * 
   * @param id - repertoire id
   * @return - A RepertoiresModel object
   */
  public RepertoiresModel findById(UUID id);

  /**
   * update repertoire info
   * 
   * @param repertoiresPutDto - data to be updated
   * @param id                - repertoire id
   */
  public void updateRepertoire(UUID id, RepertoiresPutDto repertoiresPutDto);

  /**
   * deletes a repertoire by id
   * 
   * @param id - repertoire id
   */
  public void deleteById(UUID id);
}
