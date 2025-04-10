package com.bandsyncapi.bandsyncapi.api.v1.services.impl;

import org.springframework.stereotype.Service;

import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalBandsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.UsersModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.UsersMusicalBandsModel;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.UsersMusicalBandsRepository;
import com.bandsyncapi.bandsyncapi.api.v1.services.UsersMusicalBandsService;

import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of UsersMusicalBandsService
 */
@Service
@Slf4j
public class UsersMusicalBandsServiceImpl implements UsersMusicalBandsService {

  private final UsersMusicalBandsRepository usersMusicalBandsRepository;

  /**
   * Constructor
   * 
   * @param usersMusicalBandsRepository - UsersMusicalBands Repository
   */
  public UsersMusicalBandsServiceImpl (UsersMusicalBandsRepository usersMusicalBandsRepository) {
    this.usersMusicalBandsRepository = usersMusicalBandsRepository;
  }

  @Override
  public UsersMusicalBandsModel save(UsersModel user, MusicalBandsModel musicalBand) {
    log.info("Saving user {} to musical band {}", user.getId(), musicalBand.getId());
    return usersMusicalBandsRepository.save(new UsersMusicalBandsModel(user, musicalBand, true));
  }
  
}
