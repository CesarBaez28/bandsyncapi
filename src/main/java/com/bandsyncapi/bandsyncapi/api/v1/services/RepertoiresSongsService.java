package com.bandsyncapi.bandsyncapi.api.v1.services;

import java.util.List;

import com.bandsyncapi.bandsyncapi.api.v1.models.RepertoiresModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.RepertoiresSongsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.SongsModel;

/**
 * Defines method for RepertoiresSongsModel
 */
public interface RepertoiresSongsService {

  /**
   * Save a list of RepertoiresSongsModels
   * 
   * @param repertoiresModel - repertoire
   * @param songs - songs
   * @return - A RepertoiresSongsModels list
   */
  List<RepertoiresSongsModel> saveAll (RepertoiresModel repertoiresModel, List<SongsModel> songsModels);
}
