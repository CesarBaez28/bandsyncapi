package com.bandsyncapi.bandsyncapi.api.v1.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bandsyncapi.bandsyncapi.api.v1.models.PermissionsModel;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.PermissionsRepository;
import com.bandsyncapi.bandsyncapi.api.v1.services.PermissionsService;

import jakarta.persistence.EntityNotFoundException;

/**
 * This class implements the PermissionsService interface
 */
@Service
public class PermissionsServiceImpl implements PermissionsService {

  private final PermissionsRepository permissionsRepository;

  /**
   * Constructor
   * 
   * @param permissionsRepository - PermissionsRepository object
   */
  public PermissionsServiceImpl(PermissionsRepository permissionsRepository) {
    this.permissionsRepository = permissionsRepository;
  }

  @Override
  public List<PermissionsModel> findAll() {
    return permissionsRepository.findAll();
  }

  @Override
  public PermissionsModel findById(Integer id) {
    return permissionsRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Permission not found"));
  }
}
