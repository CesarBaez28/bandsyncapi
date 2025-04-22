package com.bandsyncapi.bandsyncapi.api.v1.services.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bandsyncapi.bandsyncapi.api.v1.models.PermissionsModel;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.PermissionsRepository;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class PermissionsServiceImplTest {
  
  @Mock
  private PermissionsRepository permissionsRepository;

  @InjectMocks
  private PermissionsServiceImpl permissionsServiceImpl;

  @Test
  void testFindAll () {
    // When
    permissionsServiceImpl.findAll();

    // Then
    verify(permissionsRepository).findAll();
  }

  @Test
  void testFindById () {
    // Given
    Integer id = 1;

    given(permissionsRepository.findById(id)).willReturn(Optional.of(new PermissionsModel(id)));

    // When
    permissionsServiceImpl.findById(id);

    // Then
    verify(permissionsRepository).findById(id);
  }

  @Test
  void testFindByIdNotFound () {
    // Given
    Integer id = 1;

    given(permissionsRepository.findById(id)).willReturn(Optional.empty());

    // Then
    assertThrows(EntityNotFoundException.class, () -> {
      permissionsServiceImpl.findById(id);
    });
  }
}
