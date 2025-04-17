package com.bandsyncapi.bandsyncapi.api.v1.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.bandsyncapi.bandsyncapi.api.v1.models.ArtistsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalBandsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalGenresModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.SongsModel;

@DataJpaTest
class SongsRepositoryTest {

  @Autowired
  private SongsRepository songsRepository;

  @Autowired
  private MusicalBandsRepository musicalBandsRepository;

  @Autowired
  private ArtistsRepository artistsRepository;

  @Autowired
  private MusicalGenresRepository musicalGenresRepository;

  @Test
  void testFindByMusicalBandId() {
    // Given
    var musicalBand = MusicalBandsModel.builder()
        .name("Test Band")
        .logo("test_logo.png")
        .address("Test Address")
        .phone("123456789")
        .email("testEmail@gmail.com")
        .status(true)
        .build();
    var savedMusicalBand = musicalBandsRepository.save(musicalBand);

    var artist = artistsRepository.save(
        ArtistsModel.builder()
            .name("Test Artist")
            .musicalBand(savedMusicalBand)
            .status(true)
            .build());

    var genre = musicalGenresRepository.save(
        MusicalGenresModel.builder()
            .name("Test Genre")
            .musicalBand(savedMusicalBand)
            .status(true)
            .build());

    songsRepository.save(
        SongsModel.builder()
            .name("Test Song")
            .artist(artist)
            .genre(genre)
            .musicalBand(savedMusicalBand)
            .tonality("C")
            .link("test_link")
            .sheetMusic("test_sheet_music")
            .status(true)
            .build());

    // When
    List<SongsModel> result = songsRepository.findByMusicalBandId(savedMusicalBand.getId());

    // Then
    assertNotNull(result);
    assertFalse(result.isEmpty());
  }

  @Test
  void testUpdateSong() {
    // Given
    var musicalBand = MusicalBandsModel.builder()
        .name("Test Band")
        .logo("test_logo.png")
        .address("Test Address")
        .phone("123456789")
        .email("testEmail@gmail.com")
        .status(true)
        .build();
    var savedMusicalBand = musicalBandsRepository.save(musicalBand);

    var artist = artistsRepository.save(
        ArtistsModel.builder()
            .name("Test Artist")
            .musicalBand(savedMusicalBand)
            .status(true)
            .build());

    var genre = musicalGenresRepository.save(
        MusicalGenresModel.builder()
            .name("Test Genre")
            .musicalBand(savedMusicalBand)
            .status(true)
            .build());

    var song = songsRepository.save(
        SongsModel.builder()
            .name("Test Song")
            .artist(artist)
            .genre(genre)
            .musicalBand(savedMusicalBand)
            .tonality("C")
            .link("test_link")
            .sheetMusic("test_sheet_music")
            .status(true)
            .build());

    // When
    int result = songsRepository.updateSong(
        song.getId(),
        "Updated Song",
        artist,
        genre,
        "D",
        "updated_link",
        "updated_sheet_music");
        
    // Then
    assertEquals(1, result);
  }
}
