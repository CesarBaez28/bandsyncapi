package com.bandsyncapi.bandsyncapi.api.v1.repositories;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalBandsModel;

@DataJpaTest
class MusicalBandsRepositoryTest {

  @Autowired
  private MusicalBandsRepository musicalBandsRepository;

  @Test
  void testExistsById() {

    // Given
    var musicalBand = MusicalBandsModel.builder()
        .name("Test Band")
        .logo("test_logo.png")
        .address("Test Address")
        .phone("123456789")
        .email("testEmail@gmail.com")
        .status(true)
        .build();
    musicalBandsRepository.save(musicalBand);

    // When
    boolean exists = musicalBandsRepository.existsById(musicalBand.getId());

    // Then
    assertTrue(exists);
  }

  @Test
  void testNotExistsById() {
    // When
    boolean exists = musicalBandsRepository.existsById(UUID.randomUUID());

    // Then
    assertTrue(!exists);
  }
}
