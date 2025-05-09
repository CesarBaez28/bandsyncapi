package com.bandsyncapi.bandsyncapi.api.v1.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalBandsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.RolesModel;

@DataJpaTest
class RolesRepositoryTest {
  
  @Autowired
  private RolesRepository rolesRepository;

  @Autowired
  private MusicalBandsRepository musicalBandsRepository;

  @Test
  void testUpdateRoleNameById() {
    // Given

    var musicalBand = MusicalBandsModel.builder()
        .name("Test Band")
        .hyphenatedName("Test-band")
        .logo("test_logo.png")
        .address("Test Address")
        .phone("123456789")
        .email("testEmail@gmail.com")
        .status(true)
        .build();
    var savedMusicalBand = musicalBandsRepository.save(musicalBand);

    var role = RolesModel.builder()
    .name("Role Test")
    .musicalBand(savedMusicalBand)
    .status(true)
    .build();

    var savedRole = rolesRepository.save(role);

    // When 
    int result = rolesRepository.updateRoleNameById("Updated Role name", savedRole.getId());

    // Then
    assertEquals(1, result);
  }
}
