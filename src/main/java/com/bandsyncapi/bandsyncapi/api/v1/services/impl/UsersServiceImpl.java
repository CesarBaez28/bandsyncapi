package com.bandsyncapi.bandsyncapi.api.v1.services.impl;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.bandsyncapi.bandsyncapi.api.v1.dto.users.UsersPutDto;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalBandsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.RolesModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.UsersModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.UsersMusicalBandsModel;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.MusicalBandsRepository;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.UsersMusicalBandsRepository;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.UsersRepository;
import com.bandsyncapi.bandsyncapi.api.v1.services.UsersService;
import com.bandsyncapi.bandsyncapi.utils.Encrypt;

import jakarta.persistence.EntityNotFoundException;

/**
 * Implementation of UsersService
 */
@Service
public class UsersServiceImpl implements UsersService {

    private final MusicalBandsRepository musicalBandsRepository;

  private final UsersRepository usersRepository;

  private final UsersMusicalBandsRepository usersMusicalBandsRepository;

  private final Encrypt encrypt;

  private static final Integer DEFAULT_ROLE = 2; // Default role when a new user is created

  /**
   * Constructor
   * 
   * @param usersRepository - Users Repository
   * @param encrypt - Class to encrypt passwords
   */
  public UsersServiceImpl(UsersRepository usersRepository, UsersMusicalBandsRepository usersMusicalBandsRepository, Encrypt encrypt, MusicalBandsRepository musicalBandsRepository) {
    this.usersRepository = usersRepository;
    this.usersMusicalBandsRepository = usersMusicalBandsRepository;
    this.encrypt = encrypt;
    this.musicalBandsRepository = musicalBandsRepository;
  }

  @Override
  public UsersModel register(UsersModel usersModel) {
    String encryptedPassword = encrypt.encryptPassword(usersModel.getPassword());    
    usersModel.setPassword(encryptedPassword);

    usersModel.setRole(new RolesModel(DEFAULT_ROLE)); 

    return usersRepository.save(usersModel);
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
