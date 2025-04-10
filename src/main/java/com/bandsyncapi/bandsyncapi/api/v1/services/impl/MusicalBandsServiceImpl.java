package com.bandsyncapi.bandsyncapi.api.v1.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bandsyncapi.bandsyncapi.api.v1.dto.musicalbands.MusicalBandsDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.musicalbands.MusicalBandsPostDto;
import com.bandsyncapi.bandsyncapi.api.v1.mappers.MusicalBandsMapper;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalBandsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.RolesModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.RolesPermissionsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.UsersRolesModel;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.MusicalBandsRepository;
import com.bandsyncapi.bandsyncapi.api.v1.services.MusicalBandsService;
import com.bandsyncapi.bandsyncapi.api.v1.services.PermissionsService;
import com.bandsyncapi.bandsyncapi.api.v1.services.RolesPermissionsService;
import com.bandsyncapi.bandsyncapi.api.v1.services.RolesService;
import com.bandsyncapi.bandsyncapi.api.v1.services.UsersMusicalBandsService;
import com.bandsyncapi.bandsyncapi.api.v1.services.UsersRolesService;

import lombok.extern.slf4j.Slf4j;

/**
 * This class is a service implementation of the MusicalBandsService interface.
 */
@Service
@Slf4j
public class MusicalBandsServiceImpl implements MusicalBandsService {

  private final MusicalBandsRepository musicalBandsRepository;

  private final UsersMusicalBandsService usersMusicalBandsService;

  private final RolesService rolesService;

  private final UsersRolesService usersRolesService;

  private final MusicalBandsMapper musicalBandsMapper;

  private final PermissionsService permissionsService;

  private final RolesPermissionsService rolesPermissionsService;

  private static final String ADMIN_ROLE_NAME = "Administrador";

  /**
   * Constructor
   * 
   * @param musicalBandsRepository   - Repository for MusicalBandsModel
   * @param usersMusicalBandsService - Service for UsersMusicalBandsModel
   * @param rolesService             - Service for RolesModel
   * @param usersRolesService        - Service for UsersRolesModel
   * @param musicalBandsMapper       - Mapper for MusicalBandsModel
   */
  public MusicalBandsServiceImpl(MusicalBandsRepository musicalBandsRepository,
      UsersMusicalBandsService usersMusicalBandsService, RolesService rolesService, UsersRolesService usersRolesService,
      PermissionsService permissionsService, RolesPermissionsService rolesPermissionsService,
      MusicalBandsMapper musicalBandsMapper) {
    this.musicalBandsRepository = musicalBandsRepository;
    this.usersMusicalBandsService = usersMusicalBandsService;
    this.rolesService = rolesService;
    this.usersRolesService = usersRolesService;
    this.permissionsService = permissionsService;
    this.rolesPermissionsService = rolesPermissionsService;
    this.musicalBandsMapper = musicalBandsMapper;
  }

  @Override
  public Optional<MusicalBandsModel> findById(UUID id) {
    log.info("Finding musical band by id: {}", id);

    return musicalBandsRepository.findById(id);
  }

  @Override
  public MusicalBandsModel save(MusicalBandsModel musicalBandsModel) {
    log.info("Saving musical band: {}", musicalBandsModel);

    return musicalBandsRepository.save(musicalBandsModel);
  }

  @Transactional
  @Override
  public MusicalBandsDto registerMusicalBand(MusicalBandsPostDto musicalBandsPostDto) {
    log.info("Registering musical band...", musicalBandsPostDto);

    MusicalBandsModel musicalBandsModel = musicalBandsMapper.toModel(musicalBandsPostDto);

    // Save the new musical band
    MusicalBandsModel savedMusicalBandsModel = save(musicalBandsModel);

    log.info("Saved musical band: {}", savedMusicalBandsModel);

    // Save relationship between the user and the musical band
    usersMusicalBandsService.save(musicalBandsPostDto.user(), savedMusicalBandsModel);

    log.info("Saved relationship between user and musical band: {}", musicalBandsPostDto.user(),
        savedMusicalBandsModel);

    // Save the role of the user in the musical band
    RolesModel role = rolesService.save(RolesModel.builder()
        .name(ADMIN_ROLE_NAME)
        .musicalBand(savedMusicalBandsModel)
        .status(true).build());

    log.info("Saved role of the user in the musical band: {}", role);

    // Save relationship between the user and the role
    usersRolesService.save(new UsersRolesModel(role, savedMusicalBandsModel, musicalBandsPostDto.user(), true));

    log.info("Saved relationship between user and role: {}", musicalBandsPostDto.user(), role);

    // Get All permissions to be added to the role
    List<RolesPermissionsModel> rolesPermissions = permissionsService.findAll().stream()
        .map(permission -> new RolesPermissionsModel(role, permission, true)).toList();

    log.info("Getting all permissions to be added to the role: {}", rolesPermissions);

    // Save all permissions to the role
    rolesPermissionsService.saveAll(rolesPermissions);

    log.info("Saved all permissions to the role: {}", rolesPermissions);

    return musicalBandsMapper.toDto(savedMusicalBandsModel);
  }

  @Override
  public boolean existsById(UUID id) {
    return musicalBandsRepository.existsById(id);
  }
}
