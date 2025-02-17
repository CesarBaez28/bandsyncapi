package com.bandsyncapi.bandsyncapi.api.v1.services;

import java.util.List;
import java.util.UUID;

import com.bandsyncapi.bandsyncapi.api.v1.dto.repertoires.RepertoiresPutDto;
import com.bandsyncapi.bandsyncapi.api.v1.models.RepertoiresModel;

public interface RepertoiresService {

  /**
   * Save a new repertoire
   * 
   * @param repertoiresModel - repertoire object to be save
   * @return - the repertoire saved
   */
  public RepertoiresModel save (RepertoiresModel repertoiresModel);

  /**
   * Finds repertoires by musicalBandId
   * 
   * @param musicalBandid - musical band id
   * @return - A repertoires list
   */
  public List<RepertoiresModel> findByMusicalBandId(UUID musicalBandId);

  /**
   * update repertoire info
   * 
   * @param repertoiresPutDto - data to be updated
   * @param id - repertoire id
   */
  public void updateRepertoire (UUID id, RepertoiresPutDto repertoiresPutDto);

  /**
   * deletes a repertoire by id
   * 
   * @param id - repertoire id
   */
  public void deleteById (UUID id);
}
