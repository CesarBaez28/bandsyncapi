package com.bandsyncapi.bandsyncapi.api.v1.repositories;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalBandsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalRolesModel;

@DataJpaTest
class MusicalRolesRepositoryTest {

  @Autowired
  private MusicalRolesRepository musicalRolesRepository;

  @Autowired
  private MusicalBandsRepository musicalBandsRepository;

  @Test
  void testFindByMusicalBandId() {

    // Given
    var musicalBand = MusicalBandsModel.builder()
        .name("Test Band")
        .hyphenatedName("Test-band")
        .address("Test Address")
        .email("test@gmail.com")
        .logo("Test logo")
        .phone("Test phone")
        .status(true)
        .build();
    var musicalBandSaved = musicalBandsRepository.save(musicalBand);

    var musicalRole = MusicalRolesModel.builder()
        .name("Test Role")
        .musicalBand(musicalBand)
        .status(true)
        .build();
    musicalRolesRepository.save(musicalRole);

    // When
    List<MusicalRolesModel> musicalRoles = musicalRolesRepository.findByMusicalBandId(musicalBandSaved.getId());

    // Then
    assertNotNull(musicalRoles);
    assertFalse(musicalRoles.isEmpty());
  }
}
