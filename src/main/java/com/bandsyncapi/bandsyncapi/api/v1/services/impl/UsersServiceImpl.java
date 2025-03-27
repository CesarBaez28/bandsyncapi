package com.bandsyncapi.bandsyncapi.api.v1.services.impl;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.bandsyncapi.bandsyncapi.api.v1.dto.users.UsersPutDto;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalBandsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.UsersModel;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.UsersRepository;
import com.bandsyncapi.bandsyncapi.api.v1.services.UsersMusicalBandsService;
import com.bandsyncapi.bandsyncapi.api.v1.services.UsersService;
import com.bandsyncapi.bandsyncapi.utils.Encrypt;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

/**
 * Implementation of UsersService
 */
@Service
public class UsersServiceImpl implements UsersService {

  private final UsersRepository usersRepository;

  private final UsersMusicalBandsService usersMusicalBandsService;

  private final Encrypt encrypt;

  /**
   * Constructor
   * 
   * @param usersRepository            - Repository for UsersModel
   * @param usersMusicalBandsService   - Service for UsersMusicalBandsModel
   * @param encrypt                    - Encrypt utility
   */
  public UsersServiceImpl(UsersRepository usersRepository, UsersMusicalBandsService usersMusicalBandsService,
      Encrypt encrypt) {
    this.usersRepository = usersRepository;
    this.usersMusicalBandsService = usersMusicalBandsService;
    this.encrypt = encrypt;
  }

  @Override
  public UsersModel register(UsersModel usersModel) {
    String encryptedPassword = encrypt.encryptPassword(usersModel.getPassword());
    usersModel.setPassword(encryptedPassword);

    return usersRepository.save(usersModel);
  }

  @Transactional
  @Override
  public void joinUserToMusicalBand(UUID userId, UUID musicalBandId) {
    var user = new UsersModel(userId);
    var musicalBand = new MusicalBandsModel(musicalBandId);

    // Save relationship between the user and the musical band
    usersMusicalBandsService.save(user, musicalBand);
  }

  @Override
  public boolean existsByEmail(String email) {
    return usersRepository.existsByEmail(email);
  }

  @Override
  public List<UsersModel> getAllUsersByMusicalBandId(UUID musicalBandId) {

    List<UsersModel> users = usersRepository.findAllByMusicalBandId(musicalBandId);

    if (users.isEmpty()) {
      throw new NoSuchElementException("No se encontraron datos");
    }

    return users;
  }

  @Override
  public UsersModel getById(UUID userId) {
    return usersRepository.findById(userId)
        .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
  }

  @Override
  public void updateUser(UUID userId, UsersPutDto usersPutDto) {
    int rowUpdated = usersRepository.updateUser(userId, usersPutDto);

    if (rowUpdated == 0) {
      throw new EntityNotFoundException("Usuario no encontrado");
    }
  }
}
