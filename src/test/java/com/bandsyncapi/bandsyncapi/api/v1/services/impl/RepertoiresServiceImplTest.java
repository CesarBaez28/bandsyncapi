package com.bandsyncapi.bandsyncapi.api.v1.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bandsyncapi.bandsyncapi.api.v1.dto.repertoires.RepertoiresPutDto;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalBandsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.RepertoiresModel;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.EventsRepository;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.RepertoiresRepository;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.RepertoiresSongsRepository;
import com.bandsyncapi.bandsyncapi.api.v1.services.RepertoiresSongsService;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class RepertoiresServiceImplTest {

  @Mock
  private RepertoiresRepository repertoiresRepository;

  @Mock
  private RepertoiresSongsService repertoiresSongsService;

  @Mock
  private EventsRepository eventsRepository;

  @Mock
  private RepertoiresSongsRepository repertoiresSongsRepository;

  @InjectMocks
  private RepertoiresServiceImpl repertoiresServiceImpl;

  @Test
  void testSave() {
    // Given
    var repertoire = RepertoiresModel.builder()
        .name("Test name")
        .description("Test description")
        .musicalBand(new MusicalBandsModel(UUID.randomUUID()))
        .link("http://localhost")
        .status(true)
        .build();

    // When
    repertoiresServiceImpl.save(repertoire);

    // Then
    ArgumentCaptor<RepertoiresModel> captor = ArgumentCaptor.forClass(RepertoiresModel.class);

    verify(repertoiresRepository).save(captor.capture());

    RepertoiresModel capturedRepertoire = captor.getValue();

    assertEquals(repertoire, capturedRepertoire);
  }

  @Test
  void testFindByMusicalBandId() {
    // Given
    var musicalBandId = UUID.randomUUID();

    // When
    repertoiresServiceImpl.findByMusicalBandId(musicalBandId);

    // Then
    verify(repertoiresRepository).findByMusicalBandId(musicalBandId);
  }

  @Test
  void testUpdateRepertoire() {
    // Given
    var repertoireId = UUID.randomUUID();
    var repertoirePutDto = new RepertoiresPutDto(
        "New name",
        "New description",
        "http://localhost",
        true,
        new ArrayList<>());

    given(repertoiresRepository.updateRepertoire(
        repertoireId,
        repertoirePutDto.name(),
        repertoirePutDto.description(),
        repertoirePutDto.link(),
        repertoirePutDto.status())).willReturn(1);

    // When
    repertoiresServiceImpl.updateRepertoire(repertoireId, repertoirePutDto);

    // Then
    verify(repertoiresRepository).updateRepertoire(
        repertoireId,
        repertoirePutDto.name(),
        repertoirePutDto.description(),
        repertoirePutDto.link(),
        repertoirePutDto.status());
  }

  @Test
  void updateRepertoireNotFound() {
    // Given
    var repertoireId = UUID.randomUUID();
    var repertoirePutDto = new RepertoiresPutDto(
        "New name",
        "New description",
        "http://localhost",
        true,
        new ArrayList<>());

    given(repertoiresRepository.updateRepertoire(
        repertoireId,
        repertoirePutDto.name(),
        repertoirePutDto.description(),
        repertoirePutDto.link(),
        repertoirePutDto.status())).willReturn(0);

    // Then
    assertThrows(EntityNotFoundException.class, () -> {
      repertoiresServiceImpl.updateRepertoire(repertoireId, repertoirePutDto);
    });
  }

  @Test
  void testDeleteRepertoireId() {
    // Give
    var repertoireId = UUID.randomUUID();

    // When
    repertoiresServiceImpl.deleteById(repertoireId);

    // Then
    verify(repertoiresRepository).deleteById(repertoireId);
  }
}
