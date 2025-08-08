package com.bandsyncapi.bandsyncapi.api.v1.services.impl;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.bandsyncapi.bandsyncapi.api.v1.dto.musicalbands.MusicalBandsDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.musicalbands.MusicalBandsPostDto;
import com.bandsyncapi.bandsyncapi.api.v1.mappers.MusicalBandsMapper;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalBandsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.RolesModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.RolesPermissionsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.UsersRolesModel;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.MusicalBandsRepository;
import com.bandsyncapi.bandsyncapi.api.v1.services.FilesService;
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

  private final FilesService filesService;

  @Value("${aws.bucket.logos.directory}")
  private String awsLogosDirectory;

  private static final String OWNER_ROLE_NAME = "Propietario";

  /**
   * Constructor
   * 
   * @param musicalBandsRepository   - Repository for MusicalBandsModel
   * @param usersMusicalBandsService - Service for UsersMusicalBandsModel
   * @param rolesService             - Service for RolesModel
   * @param usersRolesService        - Service for UsersRolesModel
   * @param musicalBandsMapper       - Mapper for MusicalBandsModel
   * @param filesService             - Service to upload images
   */
  public MusicalBandsServiceImpl(MusicalBandsRepository musicalBandsRepository,
      UsersMusicalBandsService usersMusicalBandsService, RolesService rolesService, UsersRolesService usersRolesService,
      PermissionsService permissionsService, RolesPermissionsService rolesPermissionsService,
      MusicalBandsMapper musicalBandsMapper, FilesService filesService) {
    this.musicalBandsRepository = musicalBandsRepository;
    this.usersMusicalBandsService = usersMusicalBandsService;
    this.rolesService = rolesService;
    this.usersRolesService = usersRolesService;
    this.permissionsService = permissionsService;
    this.rolesPermissionsService = rolesPermissionsService;
    this.musicalBandsMapper = musicalBandsMapper;
    this.filesService = filesService;
  }

  @Override
  public Optional<MusicalBandsModel> findById(UUID id) {
    log.info("Finding musical band by id: {}", id);

    return musicalBandsRepository.findById(id);
  }

  @Override
  public MusicalBandsModel save(MusicalBandsModel musicalBandsModel) {

    musicalBandsModel.setHyphenatedName(hyphenateName(musicalBandsModel.getName()));

    log.info("Saving musical band: {}", musicalBandsModel);

    return musicalBandsRepository.save(musicalBandsModel);
  }

  @Transactional
  @Override
  public MusicalBandsDto registerMusicalBand(MusicalBandsPostDto musicalBandsPostDto, MultipartFile imagefile)
      throws IOException {
    log.info("Registering musical band...", musicalBandsPostDto);

    MusicalBandsModel musicalBandsModel = musicalBandsMapper.toModel(musicalBandsPostDto);

    musicalBandsModel.setLogo("");

    // Save the new musical band
    MusicalBandsModel savedMusicalBandsModel = save(musicalBandsModel);

    log.info("Saved musical band: {}", savedMusicalBandsModel);
    
    // Save relationship between the user and the musical band
    usersMusicalBandsService.save(musicalBandsPostDto.user(), savedMusicalBandsModel);

    log.info("Saved relationship between user and musical band: {}", musicalBandsPostDto.user(),
        savedMusicalBandsModel);

    // Save the role of the user in the musical band
    RolesModel role = rolesService.save(RolesModel.builder()
        .name(OWNER_ROLE_NAME)
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

    String fileUrl = filesService.uploadFile(imagefile, awsLogosDirectory);

    if (!fileUrl.isEmpty()) {
      updateLogoById(savedMusicalBandsModel.getId(), fileUrl);
      savedMusicalBandsModel.setLogo(fileUrl);
    }

    return musicalBandsMapper.toDto(savedMusicalBandsModel);
  }

  @Override
  public boolean existsById(UUID id) {
    return musicalBandsRepository.existsById(id);
  }

  @Override
  public Optional<MusicalBandsModel> findByHyphenatedName(String name) {
    return musicalBandsRepository.findByHyphenatedName(name);
  }

  @Override
  public void updateLogoById(UUID musicalBandId, String logo) {
    musicalBandsRepository.updateLogoById(musicalBandId, logo);
  }

  /**
   * Hyphenate a name
   * 
   * @param name - name
   * @return - hyphenated name
   */
  private String hyphenateName(String name) {
    if (name == null || name.trim().isEmpty()) {
      return "";
    }

    String[] words = name.trim().split("\\s+");
    return String.join("-", words);
  }
}
