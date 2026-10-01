package com.bandsyncapi.bandsyncapi.api.v1.services.impl;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.bandsyncapi.bandsyncapi.api.v1.models.PermissionsModel;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.PermissionsRepository;
import com.bandsyncapi.bandsyncapi.api.v1.services.PermissionsService;
import com.bandsyncapi.bandsyncapi.constants.CacheNames;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

/**
 * This class implements the PermissionsService interface
 */
@Service
@Slf4j
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
  @Cacheable(cacheNames = CacheNames.PERMISSIONS_ALL)
  public List<PermissionsModel> findAll() {
    log.info("Fetching all permissions");
    return permissionsRepository.findAll();
  }

  @Override
  @Cacheable(cacheNames = CacheNames.PERMISSIONS_BY_ID, key = "#id")
  public PermissionsModel findById(Integer id) {
    log.info("Fetching permission with id {}", id);
    return permissionsRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Permission not found"));
  }
}
