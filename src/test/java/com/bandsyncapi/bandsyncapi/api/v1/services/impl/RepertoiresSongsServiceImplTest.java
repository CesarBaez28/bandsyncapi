package com.bandsyncapi.bandsyncapi.api.v1.services.impl;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bandsyncapi.bandsyncapi.api.v1.models.ArtistsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalBandsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalGenresModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.RepertoiresModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.RepertoiresSongsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.SongsModel;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.RepertoiresSongsRepository;

@ExtendWith(MockitoExtension.class)
class RepertoiresSongsServiceImplTest {

  @Mock
  private RepertoiresSongsRepository repertoiresSongsRepository;

  @InjectMocks
  private RepertoiresSongsServiceImpl repertoiresSongsServiceImpl;

  @Test
  void testSaveAll() {
    // Given
    var musicalBand = UUID.randomUUID();
    var repertoire = RepertoiresModel.builder()
        .name("Test name")
        .description("Description Test")
        .musicalBand(new MusicalBandsModel(musicalBand))
        .link("http://localhost")
        .status(true)
        .build();

    var song = SongsModel.builder()
        .name("Test name")
        .artist(new ArtistsModel(1))
        .genre(new MusicalGenresModel(1))
        .musicalBand(new MusicalBandsModel(musicalBand))
        .link("http://localhost")
        .sheetMusic("sheet music")
        .tonality("G")
        .status(true)
        .build();

    List<SongsModel> songsList = List.of(song);

    List<RepertoiresSongsModel> repertoiresSongsModels = songsList.stream()
        .map(value -> new RepertoiresSongsModel(repertoire, value, true)).toList();

    given(repertoiresSongsRepository.saveAll(anyList())).willReturn(repertoiresSongsModels);

    // When
    repertoiresSongsServiceImpl.saveAll(repertoire, songsList);

    // Then
    verify(repertoiresSongsRepository).saveAll(anyList());
  }
}
