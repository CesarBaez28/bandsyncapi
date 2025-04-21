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
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalGenresModel;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.MusicalGenresRepository;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class MusicalGenresServiceImplTest {

  @Mock
  private MusicalGenresRepository musicalGenresRepository;

  @InjectMocks
  private MusicalGenresServiceImpl musicalGenresServiceImpl;

  @Test
  void testFindAll () {
    // When
    musicalGenresServiceImpl.findAll();

    // then
    verify(musicalGenresRepository).findAll();
  }

  @Test
  void testFindByMusicalBandId () {
    // Given
    var musicalBandId = UUID.randomUUID();

    // When
    musicalGenresServiceImpl.findByMusicalBandId(musicalBandId);

    // Then
    verify(musicalGenresRepository).findByMusicalBandId(musicalBandId);
  }

  @Test
  void testFindById () {
    // Given
    Integer id = 1;

    // When
    musicalGenresServiceImpl.findById(id);

    // Then
    verify(musicalGenresRepository).findById(id);
  }

  @Test
  void testSave () {
    // Given
    var musicalGenre = MusicalGenresModel.builder()
      .name("Test genre")
      .musicalBand(new MusicalBandsModel(UUID.randomUUID()))
      .status(true)
      .build();

    // When
    musicalGenresServiceImpl.save(musicalGenre);

    //Then
    ArgumentCaptor<MusicalGenresModel> captor = ArgumentCaptor.forClass(MusicalGenresModel.class);
    
    verify(musicalGenresRepository).save(captor.capture());
    
    MusicalGenresModel capturedUser = captor.getValue();

    assertEquals(musicalGenre, capturedUser);
  }

  @Test
  void testUpdateMusicalGenre () {
    // Given
    Integer id = 1;
    String name = "new name";

    given(musicalGenresRepository.updateGenreName(id, name)).willReturn(1);

    // When
     musicalGenresServiceImpl.updateGenreName(id, name);

    // Then
    verify(musicalGenresRepository).updateGenreName(id, name);
  }

  @Test
  void testUpdateMusicalGenreNotFound () {
    // Given
    Integer id = 1;
    String name = "new name";

    given(musicalGenresRepository.updateGenreName(id, name)).willReturn(0);

    // Then
    assertThrows(EntityNotFoundException.class, () -> {
      musicalGenresServiceImpl.updateGenreName(id, name);
    });
  }

  @Test
  void testDeleteById () {
    // Given
    Integer id = 1;

    // When
    musicalGenresServiceImpl.deleteById(id);

    // Then
    verify(musicalGenresRepository).deleteById(id);
  }
}
