package com.bandsyncapi.bandsyncapi.api.v1.services.impl;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.bandsyncapi.bandsyncapi.api.v1.dto.users.UserLoginPostDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.users.UsersPutDto;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalBandsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.UsersModel;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.UsersRepository;
import com.bandsyncapi.bandsyncapi.api.v1.services.UsersMusicalBandsService;
import com.bandsyncapi.bandsyncapi.api.v1.services.UsersService;
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

  private final AuthenticationManager authenticationManager;

  private final Encrypt encrypt;

  /**
   * Constructor
   * 
   * @param usersRepository          - Repository for UsersModel
   * @param usersMusicalBandsService - Service for UsersMusicalBandsModel
   * @param encrypt                  - Encrypt utility
   */
  public UsersServiceImpl(UsersRepository usersRepository, UsersMusicalBandsService usersMusicalBandsService,
      Encrypt encrypt, AuthenticationManager authenticationManager) {
    this.usersRepository = usersRepository;
    this.usersMusicalBandsService = usersMusicalBandsService;
    this.authenticationManager = authenticationManager;
    this.encrypt = encrypt;
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

    if (users.isEmpty()) {
      throw new NoSuchElementException("There are no users in this musical band");
    }

    return users;
  }

  @Override
  public UsersModel getById(UUID userId) {
    log.info("Getting user by id {}", userId);
    
    return usersRepository.findById(userId)
        .orElseThrow(() -> new EntityNotFoundException("User not found"));
  }

  @Override
  public void updateUser(UUID userId, UsersPutDto usersPutDto) {
    log.info("Updating user with id {}", userId);
    
    int rowUpdated = usersRepository.updateUser(userId, usersPutDto);

    if (rowUpdated == 0) {
      throw new EntityNotFoundException("User not found");
    }
  }

  @Override
  public UsersModel getByUsername(String username) {
    log.info("Getting user by username {}", username);
    
    return usersRepository.findByUsername(username)
        .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));
  }
}
