package com.bandsyncapi.bandsyncapi.api.v1.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.BDDMockito.given;

import java.io.IOException;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import com.bandsyncapi.bandsyncapi.api.v1.dto.songs.SongsPutDto;
import com.bandsyncapi.bandsyncapi.api.v1.models.ArtistsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalBandsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalGenresModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.SongsModel;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.SongsRepository;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class SongsServiceImplTest {

  @Mock
  private SongsRepository songsRepository;

  @InjectMocks
  private SongsServiceImpl songsServiceImpl;

  @Test
  void testSave() throws IOException {
    // Given
    var song = SongsModel.builder()
        .name("Song Name")
        .artist(new ArtistsModel(1))
        .genre(new MusicalGenresModel(1))
        .musicalBand(new MusicalBandsModel(UUID.randomUUID()))
        .tonality("C")
        .link("https://example.com")
        .sheetMusic("Sheet Music")
        .build();

    MockMultipartFile file = new MockMultipartFile(
        "image",
        "test_logo.png",
        "image/png",
        "dummy image content".getBytes());

    // When
    songsServiceImpl.save(song, file);

    // Then
    ArgumentCaptor<SongsModel> captor = ArgumentCaptor.forClass(SongsModel.class);

    verify(songsRepository).save(captor.capture());

    SongsModel savedSong = captor.getValue();
    assertEquals(song, savedSong);
  }

  @Test
  void testFindByMusicalBandId() {
    // Given
    var musicalBandId = UUID.randomUUID();

    // When
    songsServiceImpl.findByMusicalBandId(musicalBandId);

    // Then
    verify(songsRepository).findByMusicalBandId(musicalBandId);
  }

  @Test
  void testUpdateSong() throws IOException{
    // Given
    Integer id = 1;
    SongsPutDto songPutDto = new SongsPutDto(
      "Updated Song Name",
      new ArtistsModel(1),
      new MusicalGenresModel(1),
      "D",
      "https://example.com/updated",
      "Updated Sheet Music");
    
    MockMultipartFile file = new MockMultipartFile(
        "image",
        "test_logo.png",
        "image/png",
        "dummy image content".getBytes());

    given(songsRepository.updateSong(id, songPutDto.name(), songPutDto.artist(), songPutDto.genre(), songPutDto.tonality(), songPutDto.link(), songPutDto.sheetMusic())).willReturn(1);

    // When
    songsServiceImpl.updateSong(id, songPutDto, file);

    // Then
    verify(songsRepository).updateSong(id, songPutDto.name(), songPutDto.artist(), songPutDto.genre(), songPutDto.tonality(), songPutDto.link(), songPutDto.sheetMusic());
  }

  @Test
  void testUpdateSongNotFound() {
    // Given
    Integer id = 1;
    SongsPutDto songPutDto = new SongsPutDto(
      "Updated Song Name",
      new ArtistsModel(1),
      new MusicalGenresModel(1),
      "D",
      "https://example.com/updated",
      "Updated Sheet Music");

    MockMultipartFile file = new MockMultipartFile(
        "image",
        "test_logo.png",
        "image/png",
        "dummy image content".getBytes());

    given(songsRepository.updateSong(id, songPutDto.name(), songPutDto.artist(), songPutDto.genre(), songPutDto.tonality(), songPutDto.link(), songPutDto.sheetMusic())).willReturn(0);

    // When
    assertThrows(EntityNotFoundException.class, () -> {
      songsServiceImpl.updateSong(id, songPutDto, file);
    });

    // Then
    verify(songsRepository).updateSong(id, songPutDto.name(), songPutDto.artist(), songPutDto.genre(), songPutDto.tonality(), songPutDto.link(), songPutDto.sheetMusic());
  }
}
