package com.bandsyncapi.bandsyncapi.api.v1.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.bandsyncapi.bandsyncapi.api.v1.models.ArtistsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalBandsModel;

@DataJpaTest
class ArtistRepositoriesTest {

  @Autowired
  private ArtistsRepository artistsRepository;

  @Autowired
  private MusicalBandsRepository musicalBandsRepository;

  @Test
  void testFindByMusicalBandIdNotFound() {
    // Given
    UUID musicalBandId = UUID.randomUUID();

    // When
    List<ArtistsModel> artists = artistsRepository.findByMusicalBandId(musicalBandId);

    // Then
    assertNotNull(artists);
    assertTrue(artists.isEmpty());
  }

  @Test
  void testFindByMusicalBandIdFound() {
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

    var artist = ArtistsModel.builder()
        .name("Test Artist")
        .musicalBand(savedMusicalBand)
        .status(true)
        .build();

    artistsRepository.save(artist);

    // When
    List<ArtistsModel> artists = artistsRepository.findByMusicalBandId(savedMusicalBand.getId());

    // Then
    assertNotNull(artists);
    assertFalse(artists.isEmpty());
    assertEquals(savedMusicalBand.getId(), artists.get(0).getMusicalBand().getId());
  }

  @Test
  void testUpdateArtistName() {
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

    var artist = ArtistsModel.builder()
        .name("Test Artist")
        .musicalBand(savedMusicalBand)
        .status(true)
        .build();
    var savedArtist = artistsRepository.save(artist);

    // When
    int rowUpdated = artistsRepository.updateArtistName(savedArtist.getId(), "Updated Artist Name");

    // Then
    assertEquals(1, rowUpdated);
  }
}
