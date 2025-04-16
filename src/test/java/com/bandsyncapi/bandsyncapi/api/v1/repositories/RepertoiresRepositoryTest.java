package com.bandsyncapi.bandsyncapi.api.v1.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalBandsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.RepertoiresModel;

@DataJpaTest
class RepertoiresRepositoryTest {

  @Autowired
  private RepertoiresRepository repertoiresRepository;

  @Autowired
  private MusicalBandsRepository musicalBandsRepository;

  @Test
  void testFindByMusicalBandId() {

    // Given
    var musicalBand = MusicalBandsModel.builder()
        .name("Test Band")
        .logo("Logo")
        .address("Test Address")
        .email("Test Email")
        .phone("Test Phone")
        .phone("Test")
        .status(true)
        .build();
    var savedMusicalBand = musicalBandsRepository.save(musicalBand);

    var repertoire = RepertoiresModel.builder()
        .name("Test Repertoire")
        .description("Test Description")
        .link("Test Link")
        .status(true)
        .musicalBand(savedMusicalBand)
        .build();
    repertoiresRepository.save(repertoire);

    // When
    List<RepertoiresModel> repertoires = repertoiresRepository.findByMusicalBandId(savedMusicalBand.getId());

    // Then
    assertNotNull(repertoires);
    assertFalse(repertoires.isEmpty());
  }

  @Test
  void updateRepertoire() {

    // Given
    var musicalBand = MusicalBandsModel.builder()
        .name("Test Band")
        .logo("Logo")
        .address("Test Address")
        .email("Test Email")
        .phone("Test Phone")
        .phone("Test")
        .status(true)
        .build();
    var savedMusicalBand = musicalBandsRepository.save(musicalBand);

    var repertoire = RepertoiresModel.builder()
        .name("Test Repertoire")
        .description("Test Description")
        .link("Test Link")
        .status(true)
        .musicalBand(savedMusicalBand)
        .build();
    var savedRepertoire = repertoiresRepository.save(repertoire);

    // When
    int updatedRow = repertoiresRepository.updateRepertoire(
        savedRepertoire.getId(),
        "Updated Name",
        "Updated Description",
        "UpdatedLink.com",
        true);

    // Then
    assertEquals(1, updatedRow);
  }
}
