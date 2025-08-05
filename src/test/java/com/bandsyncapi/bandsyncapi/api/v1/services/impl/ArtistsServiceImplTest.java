package com.bandsyncapi.bandsyncapi.api.v1.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.BDDMockito.given;

import com.bandsyncapi.bandsyncapi.api.v1.models.ArtistsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalBandsModel;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.ArtistsRepository;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.RepertoiresSongsRepository;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.SongsRepository;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class ArtistsServiceImplTest {

  @Mock
  private ArtistsRepository artistsRepository;

  @Mock
  private SongsRepository songsRepository;

  @Mock
  private RepertoiresSongsRepository repertoiresSongsRepository;

  @InjectMocks
  private ArtistsServiceImpl artistsService;

  @Test
  void testSave () {

    // Given
    var artistsModel = ArtistsModel.builder()
        .id(1)
        .name("Test Artist")
        .musicalBand(new MusicalBandsModel(UUID.randomUUID()))
        .build();

    // When
    artistsService.save(artistsModel);

    // Then
    ArgumentCaptor<ArtistsModel> captor = ArgumentCaptor.forClass(ArtistsModel.class);

    // Verify that the save method was called on the repository with the correct argument
    verify(artistsRepository).save(captor.capture());

    ArtistsModel capturedArtist = captor.getValue();

    assertEquals(capturedArtist, artistsModel);
  }

  @Test
  void testFindByMusicalBandId () {

    // Given
    var musicalBandId = UUID.randomUUID();

    // When
    artistsService.findByMusicalBandId(musicalBandId);

    // Then
    verify(artistsRepository).findByMusicalBandId(musicalBandId);
  }

  @Test
  void updateArtist () {

    // Given
    Integer id = 1;
    String name = "Updated Artist";

    // When
    given(artistsRepository.updateArtistName(id, name)).willReturn(1);

    artistsService.updateArtist(id, name);
    
    // Then
    verify(artistsRepository).updateArtistName(id, name);
  }

  @Test
  void updateArtistNotFound () {

    // Given
    Integer id = 1;
    String name = "Updated Artist";

    // When
    given(artistsRepository.updateArtistName(id, name)).willReturn(0);

    // Then
    assertThrows(EntityNotFoundException.class, () -> {
      artistsService.updateArtist(id, name);
    });
  }

  @Test
  void testDeleteById () {

    // Given
    Integer id = 1;

    // When
    artistsService.deleteById(id);

    // Then
    verify(artistsRepository).deleteById(id);
  }
}
