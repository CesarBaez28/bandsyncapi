package com.bandsyncapi.bandsyncapi.api.v1.services.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bandsyncapi.bandsyncapi.api.v1.dto.users.ChangePasswordDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.users.UserLoginPostDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.users.UsersPutDto;
import com.bandsyncapi.bandsyncapi.api.v1.enums.InvitationStatus;
import com.bandsyncapi.bandsyncapi.api.v1.models.InvitationsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalBandsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.UsersModel;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.InvitationsRepository;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.UsersRepository;
import com.bandsyncapi.bandsyncapi.api.v1.services.FilesService;
import com.bandsyncapi.bandsyncapi.api.v1.services.UsersMusicalBandsService;
import com.bandsyncapi.bandsyncapi.api.v1.services.UsersService;
import com.bandsyncapi.bandsyncapi.utils.AwsUtils;
import com.bandsyncapi.bandsyncapi.utils.Encrypt;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of UsersService
 */
@Service
@Slf4j
public class UsersServiceImpl implements UsersService {

  private final UsersRepository usersRepository;

  private final UsersMusicalBandsService usersMusicalBandsService;

  private final InvitationsRepository invitationsRepository;

  private final AuthenticationManager authenticationManager;

  private final Encrypt encrypt;

  private final FilesService filesService;

  @Value("${aws.bucket.users.directory}")
  private String awsUsersDirectory;

  /**
   * Constructor
   * 
   * @param usersRepository
   * @param usersMusicalBandsService
   * @param invitationsRepository
   * @param encrypt
   * @param authenticationManager
   * @param filesService
   */
  public UsersServiceImpl(UsersRepository usersRepository, UsersMusicalBandsService usersMusicalBandsService,
      InvitationsRepository invitationsRepository,
      Encrypt encrypt, AuthenticationManager authenticationManager, FilesService filesService) {
    this.usersRepository = usersRepository;
    this.usersMusicalBandsService = usersMusicalBandsService;
    this.invitationsRepository = invitationsRepository;
    this.authenticationManager = authenticationManager;
    this.encrypt = encrypt;
    this.filesService = filesService;
  }

  @Override
  public boolean verify(UserLoginPostDto userLoginPostDto) {
    log.info("Authenticating user: {}", userLoginPostDto.username());

    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(userLoginPostDto.username(), userLoginPostDto.password()));

    return authentication.isAuthenticated();
  }

  @Override
  public UsersModel register(UsersModel usersModel) {
    log.info("Registering user: {}", usersModel.getUsername());

    String encryptedPassword = encrypt.encryptPassword(usersModel.getPassword());
    usersModel.setPassword(encryptedPassword);

    return usersRepository.save(usersModel);
  }

  @Override
  public UsersModel registerFromInvitation(UsersModel usersModel, String token) {
    log.info("Registering user from invitation: {}", usersModel.getUsername());

    InvitationsModel invitation = invitationsRepository.findByToken(token)
        .orElseThrow(() -> new EntityNotFoundException("Invitation not found"));

    if (!invitation.getEmail().equalsIgnoreCase(usersModel.getEmail())) {
      log.error("The email does not match the invitation email");
      throw new IllegalStateException("El correo no coincide con la invitación");
    }

    String encryptedPassword = encrypt.encryptPassword(usersModel.getPassword());
    usersModel.setPassword(encryptedPassword);

    log.info("Saving new user");
    UsersModel newUser = usersRepository.save(usersModel);

    log.info("Joining user to musical band");
    joinUserToMusicalBand(newUser.getId(), invitation.getMusicalBand().getId());

    log.info("Updating invitation status to ACCEPTED");
    invitation.setStatus(InvitationStatus.ACCEPTED);
    invitation.setExpiresAt(LocalDateTime.now());
    invitationsRepository.save(invitation);

    log.info("User registered from invitation successfully: {}", usersModel.getUsername());
    return newUser;
  }

  @Override
  public void changePassword(ChangePasswordDto changePasswordDto) {
    log.info("Changing password for user: {}", changePasswordDto.username());

    UsersModel user = usersRepository.findByUsername(changePasswordDto.username())
        .orElseThrow(
            () -> new EntityNotFoundException("User not found with username: " + changePasswordDto.username()));

    if (!encrypt.checkPassword(changePasswordDto.oldPassword(), user.getPassword())) {
      log.error("Old password does not match for user: {}", changePasswordDto.username());
      throw new IllegalStateException("La contraseña actual no coincide");
    }

    user.setPassword(encrypt.encryptPassword(changePasswordDto.newPassword()));
    usersRepository.save(user);
    log.info("Password changed successfully for user: {}", changePasswordDto.username());
  }

  @Transactional
  @Override
  public void joinUserToMusicalBand(UUID userId, UUID musicalBandId) {
    log.info("Joining user {} to musical band {}", userId, musicalBandId);

    var user = new UsersModel(userId);
    var musicalBand = new MusicalBandsModel(musicalBandId);

    // Save relationship between the user and the musical band
    usersMusicalBandsService.save(user, musicalBand);
  }

  @Override
  public boolean existsByEmail(String email) {
    log.info("Checking if email {} exists", email);
    return usersRepository.existsByEmail(email);
  }

  @Override
  public List<UsersModel> getAllUsersByMusicalBandId(UUID musicalBandId) {
    log.info("Getting all users by musical band id {}", musicalBandId);

    List<UsersModel> users = usersRepository.findAllByMusicalBandId(musicalBandId);

    return users;
  }

  @Override
  public Page<UsersModel> find(UUID musicalBandId, String term, int page, int size) {
    return usersRepository.find(musicalBandId, term, PageRequest.of(page, size, Sort.by("firstName")));
  }

  @Override
  public UsersModel getById(UUID userId) {
    log.info("Getting user by id {}", userId);

    return usersRepository.findById(userId)
        .orElseThrow(() -> new EntityNotFoundException("User not found"));
  }

  @Override
  @Transactional
  public UsersPutDto updateUser(UUID userId, UsersPutDto usersPutDto, MultipartFile imagFile) throws IOException {
    log.info("Updating user with id {}", userId);

    String fileUrl = filesService.uploadFile(imagFile, awsUsersDirectory);

    if (!fileUrl.isEmpty()) {
      String existingPhoto = usersPutDto.getPhoto();

      if (existingPhoto != null && !existingPhoto.isEmpty()) {
        String fileName = AwsUtils.getFileNameFromAwsUrl(existingPhoto);
        filesService.deleteFile(awsUsersDirectory + "/" + fileName);
      }

      usersPutDto.setPhoto(fileUrl);
    }

    int rowUpdated = usersRepository.updateUser(userId, usersPutDto);

    if (rowUpdated == 0) {
      throw new EntityNotFoundException("User not found");
    }

    return usersPutDto;
  }

  @Override
  public UsersModel getByUsername(String username) {
    log.info("Getting user by username {}", username);

    return usersRepository.findByUsername(username)
        .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));
  }

  @Override
  public Optional<UsersModel> findByEmail(String email) {
    log.info("Finding user by email: {}", email);
    return usersRepository.findByEmail(email);
  }
}
