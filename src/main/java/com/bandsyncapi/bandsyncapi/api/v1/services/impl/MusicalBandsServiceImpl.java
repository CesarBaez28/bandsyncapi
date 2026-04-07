package com.bandsyncapi.bandsyncapi.api.v1.services.impl;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.bandsyncapi.bandsyncapi.api.v1.dto.musicalbands.MusicalBandPutDto;
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
import com.bandsyncapi.bandsyncapi.api.v1.services.MusicalGenresService;
import com.bandsyncapi.bandsyncapi.api.v1.services.MusicalRolesService;
import com.bandsyncapi.bandsyncapi.api.v1.services.PermissionsService;
import com.bandsyncapi.bandsyncapi.api.v1.services.RolesPermissionsService;
import com.bandsyncapi.bandsyncapi.api.v1.services.RolesService;
import com.bandsyncapi.bandsyncapi.api.v1.services.UsersMusicalBandsService;
import com.bandsyncapi.bandsyncapi.api.v1.services.UsersRolesService;
import com.bandsyncapi.bandsyncapi.utils.AwsUtils;

import jakarta.persistence.EntityNotFoundException;
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

  private final MusicalGenresService musicalGenresService;

  private final MusicalRolesService musicalRolesService;

  @Value("${aws.bucket.logos.directory}")
  private String awsLogosDirectory;

  private static final String ADMIN_ROLE_NAME = "Administrador";

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

  /**
   * Constructor
   * 
   * @param musicalBandsRepository   - Repository for MusicalBandsModel
   * @param usersMusicalBandsService - Service for UsersMusicalBandsModel
   * @param rolesService             - Service for RolesModel
   * @param usersRolesService        - Service for UsersRolesModel
   * @param musicalBandsMapper       - Mapper for MusicalBandsModel
   * @param filesService             - Service to upload images
   * @param musicalGenresService     - Musical genres service
   */
  public MusicalBandsServiceImpl(MusicalBandsRepository musicalBandsRepository,
      UsersMusicalBandsService usersMusicalBandsService, RolesService rolesService, UsersRolesService usersRolesService,
      PermissionsService permissionsService, RolesPermissionsService rolesPermissionsService,
      MusicalBandsMapper musicalBandsMapper, FilesService filesService, MusicalGenresService musicalGenresService,
      MusicalRolesService musicalRolesService) {
    this.musicalBandsRepository = musicalBandsRepository;
    this.usersMusicalBandsService = usersMusicalBandsService;
    this.rolesService = rolesService;
    this.usersRolesService = usersRolesService;
    this.permissionsService = permissionsService;
    this.rolesPermissionsService = rolesPermissionsService;
    this.musicalBandsMapper = musicalBandsMapper;
    this.filesService = filesService;
    this.musicalGenresService = musicalGenresService;
    this.musicalRolesService = musicalRolesService;
  }

  @Override
  public MusicalBandsDto findById(UUID id) {
    log.info("Finding musical band by id: {}", id);

    MusicalBandsModel musicalBandsModel = musicalBandsRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("musical band not found"));

    return musicalBandsMapper.toDto(musicalBandsModel);
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
    var role = rolesService.save(RolesModel.builder()
        .name(ADMIN_ROLE_NAME)
        .musicalBand(savedMusicalBandsModel)
        .status(true).build());

    log.info("Saved role of the user in the musical band: {}", role);

    log.info("Saving relationship between user and role: {}", musicalBandsPostDto.user(), role);
    usersRolesService.save(new UsersRolesModel(role, savedMusicalBandsModel, musicalBandsPostDto.user(), true));

    log.info("Getting all permissions to be added to the role");
    List<RolesPermissionsModel> rolesPermissions = permissionsService.findAll().stream()
        .map(permission -> new RolesPermissionsModel(role, permission, true)).toList();

    // Save all permissions to the role
    rolesPermissionsService.saveAll(rolesPermissions);
    log.info("Saved all permissions to the role");

    musicalGenresService.insertDefaultGenres(savedMusicalBandsModel);

    musicalRolesService.insertDefaulRoles(savedMusicalBandsModel);

    String fileUrl = filesService.uploadFile(imagefile, awsLogosDirectory);

    if (!fileUrl.isEmpty()) {
      updateLogoById(savedMusicalBandsModel.getId(), fileUrl);
      savedMusicalBandsModel.setLogo(fileUrl);
    }

    return musicalBandsMapper.toDto(savedMusicalBandsModel);
  }

  @Override
  @Transactional
  public MusicalBandPutDto update(UUID musicalBandId, MusicalBandPutDto musicalBandPutDto, MultipartFile imageFile)
      throws IOException {

    log.info("Updating musical band with id {}", musicalBandId);

    String uploadedFileUrl = filesService.uploadFile(imageFile, awsLogosDirectory);
    String currentFileUrl = musicalBandPutDto.logo();

    boolean hasNewFile = uploadedFileUrl != null && !uploadedFileUrl.isBlank();
    boolean hasCurrentFile = currentFileUrl != null && !currentFileUrl.isBlank();

    String finalFileUrl;

    if (hasNewFile) {
      if (hasCurrentFile) {
        String fileName = AwsUtils.getFileNameFromAwsUrl(currentFileUrl);
        filesService.deleteFile(awsLogosDirectory + "/" + fileName);
      }
      finalFileUrl = uploadedFileUrl;
    } else {
      finalFileUrl = currentFileUrl;
    }

    int rowsUpdated = musicalBandsRepository.update(musicalBandId, musicalBandPutDto, finalFileUrl);

    if (rowsUpdated == 0) {
      throw new EntityNotFoundException("Musical band not found with id: " + musicalBandId);
    }

    return MusicalBandPutDto.builder()
        .name(musicalBandPutDto.name())
        .address(musicalBandPutDto.address())
        .email(musicalBandPutDto.email())
        .phone(musicalBandPutDto.phone())
        .logo(finalFileUrl)
        .build();
  }

  @Override
  public boolean existsById(UUID id) {
    return musicalBandsRepository.existsById(id);
  }

  @Override
  public MusicalBandsDto findByHyphenatedName(String name) {
    log.info("Finding musical band by hyphenatedName");

    MusicalBandsModel musicalBandsModel = musicalBandsRepository.findByHyphenatedName(name)
        .orElseThrow(() -> new EntityNotFoundException("musical band not found"));

    return musicalBandsMapper.toDto(musicalBandsModel);
  }

  @Override
  public void updateLogoById(UUID musicalBandId, String logo) {
    musicalBandsRepository.updateLogoById(musicalBandId, logo);
  }

  @Override
  public void deleteById(UUID musicalBandId) {
    log.info("Deleting musical band with id: {}", musicalBandId);

    MusicalBandsModel band = musicalBandsRepository.findById(musicalBandId)
        .orElseThrow(() -> new EntityNotFoundException("Musical band not found with id: " + musicalBandId));

    if (band.getLogo() != null && !band.getLogo().isEmpty()) {
      log.info("Deleting logo in aws of musical band with id: {}", musicalBandId);
      String fileName = AwsUtils.getFileNameFromAwsUrl(band.getLogo());
      String fileLocation = (awsLogosDirectory + "/" + fileName).trim();
      filesService.deleteFile(fileLocation);
    }

    musicalBandsRepository.deleteByBandId(musicalBandId);
  }
}
