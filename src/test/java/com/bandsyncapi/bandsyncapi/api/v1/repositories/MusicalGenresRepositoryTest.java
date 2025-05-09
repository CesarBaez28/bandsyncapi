package com.bandsyncapi.bandsyncapi.api.v1.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalBandsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalGenresModel;

@DataJpaTest
class MusicalGenresRepositoryTest {

  @Autowired
  private MusicalGenresRepository musicalGenresRepository;

  @Autowired
  private MusicalBandsRepository musicalBandRepository;

  @Test
  void testFindByMusicalBandId () {
    
    // Given
    var musicalBand = MusicalBandsModel.builder()
      .address("Test Address")
      .hyphenatedName("Test-band")
      .email("test@hotmail.com")
      .logo("testLogo")
      .name("Test Band")
      .phone("123456789")
      .status(true)
      .build();
    var savedMusicalBand = musicalBandRepository.save(musicalBand);

    var musicalGenres = MusicalGenresModel.builder()
      .name("Test Genre")
      .musicalBand(savedMusicalBand)
      .status(true)
      .build();
    musicalGenresRepository.save(musicalGenres);

    // When
    List<MusicalGenresModel> musicalGenresList = musicalGenresRepository.findByMusicalBandId(savedMusicalBand.getId());

    // Then
    assertNotNull(musicalGenresList);
    assertFalse(musicalGenresList.isEmpty());
  }

  @Test
  void testUpdateMusicalGenre () {

    // Given
    var musicalBand = MusicalBandsModel.builder()
      .address("Test Address")
      .hyphenatedName("Test-band")
      .email("test@hotmail.com")
      .logo("testLogo")
      .name("Test Band")
      .phone("123456789")
      .status(true)
      .build();
    var savedMusicalBand = musicalBandRepository.save(musicalBand);

    var musicalGenres = MusicalGenresModel.builder()
      .name("Test Genre")
      .musicalBand(savedMusicalBand)
      .status(true)
      .build();
    musicalGenresRepository.save(musicalGenres);
    
    // When
    int updatedRows = musicalGenresRepository.updateGenreName(musicalGenres.getId(), "Updated Genre");

    // Then
    assertEquals(1, updatedRows);
  }
}
