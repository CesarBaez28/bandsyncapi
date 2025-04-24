package com.bandsyncapi.bandsyncapi.api.v1.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalBandsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.RolesModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.UsersModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.UsersRolesModel;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.UsersRolesRepository;

@ExtendWith(MockitoExtension.class)
class UsersRolesServiceImplTest {

  @Mock
  private UsersRolesRepository usersRolesRepository;

  @InjectMocks
  private UsersRolesServiceImpl usersRolesServiceImpl;

  @Test
  void testSave () {
    // Given
    var role = new RolesModel(1);
    var musicalBand = new MusicalBandsModel(UUID.randomUUID());
    var user = new UsersModel(UUID.randomUUID());
    var usersRolesModel = new UsersRolesModel(role, musicalBand, user, true);

    // When
    usersRolesServiceImpl.save(usersRolesModel);

    // Then
    ArgumentCaptor<UsersRolesModel> argumentCaptor = ArgumentCaptor.forClass(UsersRolesModel.class); 

    verify(usersRolesRepository).save(argumentCaptor.capture());
    
    UsersRolesModel savedUsersRolesModel = argumentCaptor.getValue();

    assertEquals(usersRolesModel, savedUsersRolesModel);
  }
}
