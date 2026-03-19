package com.bandsyncapi.bandsyncapi.api.v1.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bandsyncapi.bandsyncapi.api.v1.mappers.MusicalBandsMapper;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalBandsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.UsersMusicalBandsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.UsersModel;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.UsersMusicalBandsRepository;

@ExtendWith(MockitoExtension.class)
class UsersMusicalBandsServiceImplTest {

  @Mock
  private UsersMusicalBandsRepository usersMusicalBandsRepository;

  @Mock
  private MusicalBandsMapper musicalBandsMapper;

  @InjectMocks
  private UsersMusicalBandsServiceImpl usersMusicalBandsServiceImpl;

  @Test
  void testSave() {
    // Given
    var user = new UsersModel(UUID.randomUUID());
    var musicalBand = new MusicalBandsModel(UUID.randomUUID());

    // When
    usersMusicalBandsServiceImpl.save(user, musicalBand);

    // Then
    ArgumentCaptor<UsersMusicalBandsModel> captor = ArgumentCaptor.forClass(UsersMusicalBandsModel.class);

    verify(usersMusicalBandsRepository).save(captor.capture());
    var usersMusicalBandsModel = captor.getValue();

    assertEquals(user.getId(), usersMusicalBandsModel.getUser().getId());
    assertEquals(musicalBand.getId(), usersMusicalBandsModel.getMusicalBand().getId());
  }

  @Test
  void testFindByUser() {
    // Given
    var user = new UsersModel(UUID.randomUUID());
    var musicalBand = new MusicalBandsModel(UUID.randomUUID());


    var userMusicalBand = new UsersMusicalBandsModel(user, musicalBand, true);
    List<UsersMusicalBandsModel> list = List.of(userMusicalBand);

    given(usersMusicalBandsRepository.findByUser(user.getId())).willReturn(list);

    // When
    usersMusicalBandsServiceImpl.findByUser(user);

    // Then
    verify(usersMusicalBandsRepository).findByUser(user.getId());
  }
}
