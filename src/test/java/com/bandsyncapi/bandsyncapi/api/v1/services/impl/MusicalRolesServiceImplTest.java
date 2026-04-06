package com.bandsyncapi.bandsyncapi.api.v1.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalBandsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalRolesModel;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.MusicalRolesRepository;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class MusicalRolesServiceImplTest {

  @Mock
  private MusicalRolesRepository musicalRolesRepository;

  @InjectMocks
  private MusicalRolesServiceImpl musicalRolesServiceImpl;

  @Test
  void testSave() {
    // Given
    var musicalRole = MusicalRolesModel.builder()
        .name("Test name")
        .musicalBand(new MusicalBandsModel(UUID.randomUUID()))
        .status(true)
        .build();

    // When
    musicalRolesServiceImpl.save(musicalRole);

    // Then
    ArgumentCaptor<MusicalRolesModel> captor = ArgumentCaptor.forClass(MusicalRolesModel.class);

    verify(musicalRolesRepository).save(captor.capture());

    MusicalRolesModel capturedMusicalRole = captor.getValue();

    assertEquals(musicalRole, capturedMusicalRole);
  }

  @Test
  void testFindByMusicalBandId() {
    // Given
    var musicalBandId = UUID.randomUUID();

    // When
    musicalRolesServiceImpl.findByMusicalBandId(musicalBandId);

    // Then
    verify(musicalRolesRepository).findByMusicalBandId(musicalBandId);
  }

  @Test
  void testUpdateMusicalRoleName() {
    // Given
    Integer id = 1;
    String name = "new name";

    given(musicalRolesRepository.updateMusicalRoleName(id, name)).willReturn(1);

    // When
    musicalRolesServiceImpl.updateMusicalRoleName(id, name);

    // Then
    verify(musicalRolesRepository).updateMusicalRoleName(id, name);
  }

  @Test
  void testUpdateMusicalRoleNotFound() {
    // Given
    Integer id = 1;
    String name = "new name";

    given(musicalRolesRepository.updateMusicalRoleName(id, name)).willReturn(0);

    // Then
    assertThrows(EntityNotFoundException.class, () -> {
      musicalRolesServiceImpl.updateMusicalRoleName(id, name);
    });
  }

  @Test
  void deleteById() {
    // Given
    Integer id = 1;

    // When
    musicalRolesServiceImpl.deleteById(id);

    // Then
    verify(musicalRolesRepository).deleteById(id);
  }
}
