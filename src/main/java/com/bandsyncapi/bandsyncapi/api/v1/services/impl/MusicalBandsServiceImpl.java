package com.bandsyncapi.bandsyncapi.api.v1.services.impl;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bandsyncapi.bandsyncapi.api.v1.dto.musicalbands.MusicalBandsDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.musicalbands.MusicalBandsPostDto;
import com.bandsyncapi.bandsyncapi.api.v1.mappers.MusicalBandsMapper;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalBandsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.RolesModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.UsersRolesModel;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.MusicalBandsRepository;
import com.bandsyncapi.bandsyncapi.api.v1.services.MusicalBandsService;
import com.bandsyncapi.bandsyncapi.api.v1.services.RolesService;
import com.bandsyncapi.bandsyncapi.api.v1.services.UsersMusicalBandsService;
import com.bandsyncapi.bandsyncapi.api.v1.services.UsersRolesService;

/**
 * This class is a service implementation of the MusicalBandsService interface.
 */
@Service
public class MusicalBandsServiceImpl implements MusicalBandsService {

  private final MusicalBandsRepository musicalBandsRepository;

  private final UsersMusicalBandsService usersMusicalBandsService;

  private final RolesService rolesService;

  private final UsersRolesService usersRolesService;

  private final MusicalBandsMapper musicalBandsMapper;

  private static final String ADMIN_ROLE_NAME = "Administrador";

  /**
   * Constructor for the MusicalBandsServiceImpl class
   * @param musicalBandsRepository - Repository with methods for performing CRUD operations on the musical_bands table.
   * @param musicalBandsMapper - Mapper to convert between MusicalBandsModel and MusicalBandsDto.
   */
  public MusicalBandsServiceImpl(MusicalBandsRepository musicalBandsRepository, UsersMusicalBandsService usersMusicalBandsService, RolesService rolesService, UsersRolesService usersRolesService, MusicalBandsMapper musicalBandsMapper) {
    this.musicalBandsRepository = musicalBandsRepository;
    this.usersMusicalBandsService = usersMusicalBandsService;
    this.rolesService = rolesService;
    this.usersRolesService = usersRolesService;
    this.musicalBandsMapper = musicalBandsMapper;
  }

  @Override
  public Optional<MusicalBandsModel> findById(UUID id) {
    return musicalBandsRepository.findById(id);
  }

  @Override
  public MusicalBandsModel save(MusicalBandsModel musicalBandsModel) {
    return musicalBandsRepository.save(musicalBandsModel);
  }

  @Transactional
  @Override
  public MusicalBandsDto registerMusicalBand(MusicalBandsPostDto musicalBandsPostDto) {

    MusicalBandsModel musicalBandsModel = musicalBandsMapper.toModel(musicalBandsPostDto);
    
    // Save the new musical band
    MusicalBandsModel savedMusicalBandsModel = save(musicalBandsModel);

    // Save relationship between the user and the musical band
    usersMusicalBandsService.save(musicalBandsPostDto.user(), savedMusicalBandsModel);

    // Save the role of the user in the musical band
    RolesModel role = rolesService.save(RolesModel.builder()
        .name(ADMIN_ROLE_NAME)
        .musicalBand(savedMusicalBandsModel)
        .status(true).build());

    // Save relationship betheen the user and the role
    usersRolesService.save(new UsersRolesModel(role, savedMusicalBandsModel, musicalBandsPostDto.user(), true));

    return musicalBandsMapper.toDto(savedMusicalBandsModel);
  }
}
