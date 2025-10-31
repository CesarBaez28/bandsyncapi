package com.bandsyncapi.bandsyncapi.api.v1.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bandsyncapi.bandsyncapi.api.v1.models.TypeOfPermissionsModel;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.TypeOfPermissionsRepository;
import com.bandsyncapi.bandsyncapi.api.v1.services.TypeOfPermissionsService;

/**
 * This class implements the TypeOfPermissionsService interface
 */
@Service
public class TypeOfPermissionsServiceImp implements TypeOfPermissionsService {

  private final TypeOfPermissionsRepository typeOfPermissionsRepository;

  /**
   * Constructor
   * 
   * @param typeOfPermissionsRepository - TypeOfPermissionsRepository object
   */
  public TypeOfPermissionsServiceImp(TypeOfPermissionsRepository typeOfPermissionsRepository) {
    this.typeOfPermissionsRepository = typeOfPermissionsRepository;
  }

  @Override
  public List<TypeOfPermissionsModel> findAll() {
    return typeOfPermissionsRepository.findAll();
  }
}
