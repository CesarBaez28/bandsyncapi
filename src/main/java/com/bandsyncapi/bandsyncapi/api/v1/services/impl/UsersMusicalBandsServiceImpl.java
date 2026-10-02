package com.bandsyncapi.bandsyncapi.api.v1.services.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.bandsyncapi.bandsyncapi.api.v1.dto.musicalbands.MusicalBandsDto;
import com.bandsyncapi.bandsyncapi.api.v1.mappers.MusicalBandsMapper;
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

  private final MusicalBandsMapper musicalBandsMapper;

  /**
   * Constructor
   * 
   * @param usersMusicalBandsRepository - UsersMusicalBands Repository
   * @param musicalBandsMapper - MusicalBands mapper
   */
  public UsersMusicalBandsServiceImpl (UsersMusicalBandsRepository usersMusicalBandsRepository, MusicalBandsMapper musicalBandsMapper) {
    this.usersMusicalBandsRepository = usersMusicalBandsRepository;
    this.musicalBandsMapper = musicalBandsMapper;
  }

  @Override
  public UsersMusicalBandsModel save(UsersModel user, MusicalBandsModel musicalBand) {
    log.info("Saving user {} to musical band {}", user.getId(), musicalBand.getId());
    return usersMusicalBandsRepository.save(new UsersMusicalBandsModel(user, musicalBand, true));
  }

  @Override
  public List<MusicalBandsDto> findByUser(UsersModel usersModel) {
    log.info("Finding musical bands by user: {} ", usersModel);
    return musicalBandsMapper.toDtoListFromUsersMusicalBand(usersMusicalBandsRepository.findByUser(usersModel.getId()));
  }  

  @Override
  public void deleteByUserIdAndMusicalBandId(UUID userId, UUID musicalBandId) {
    log.info("Deleting user {} from musical band {}", userId, musicalBandId);
    usersMusicalBandsRepository.deleteByUserIdAndMusicalBandId(userId, musicalBandId);
  }

  @Override
  public void deleteByUserId(UUID userId) {
    log.info("Deleting all relationships of user {} with musical bands", userId);
    usersMusicalBandsRepository.deleteByUserId(userId);
  }
}
