package com.bandsyncapi.bandsyncapi.api.v1.services;

import java.util.UUID;

import org.springframework.data.domain.Page;

import com.bandsyncapi.bandsyncapi.api.v1.dto.setlists.CreateSetListDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.setlists.SetListDetailsDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.setlists.UpdateSetListDto;
import com.bandsyncapi.bandsyncapi.api.v1.models.SetListsModel;

/**
 * This interface defines methods for managing set lists in the application.
 */
public interface SetListsService {

  /**
   * Finds set lists by musical band id and a search term
   * 
   * @param musicalBandId - musical band id
   * @param term          - search term
   * @param page          - page number
   * @param size          - page size
   * @return - Page of SetListsModel
   */
  Page<SetListsModel> findAllByMusicalBandId(UUID musicalBandId, String term, int page, int size);

  /**
   * Finds details of a set list by its id
   * 
   * @param setListId - set list id
   * @return - SetListDetailsDto
   */
  SetListDetailsDto findSetListDetailsById(UUID setListId);

  /**
   * Save a new set list
   * 
   * @param createSetListDto - data for for the new set list
   */
  void saveSetList(CreateSetListDto createSetListDto);

  /**
   * Update an existing set list
   * 
   * @param setListId        - set list id
   * @param updateSetListDto - data for updating the set list
   */
  void updateSetList(UUID setListId, UpdateSetListDto updateSetListDto);

  /**
   * deletes a set list
   * 
   * @param setListId - set list id
   */
  void deleteSetList(UUID setListId);
}
