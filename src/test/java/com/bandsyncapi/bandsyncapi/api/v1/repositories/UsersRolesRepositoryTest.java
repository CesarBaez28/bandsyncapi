package com.bandsyncapi.bandsyncapi.api.v1.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalBandsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.RolesModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.UsersModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.UsersRolesModel;

@DataJpaTest
class UsersRolesRepositoryTest {

  @Autowired
  private UsersRolesRepository usersRolesRepository;

  @Autowired
  private UsersRepository usersRepository;

  @Autowired
  private RolesRepository rolesRepository;

  @Autowired
  private MusicalBandsRepository musicalBandsRepository;

  @Test
  void testFindByUserIdAndMusicalBandId() {

    // Given
    var user = usersRepository.save(
        UsersModel.builder()
            .username("Test username")
            .password("Test password")
            .email("Test email")
            .firstName("Test first name")
            .lastName("Test last name")
            .phone("8093451254")
            .photo("Test photo")
            .status(true)
            .build());

    var musicalBand = musicalBandsRepository.save(
        MusicalBandsModel.builder()
            .name("Test Band")
            .hyphenatedName("Test-band")
            .logo("test_logo.png")
            .address("Test Address")
            .phone("123456789")
            .email("testEmail@gmail.com")
            .status(true)
            .build());

    var role = rolesRepository.save(
        RolesModel.builder()
            .name("Test Role")
            .musicalBand(musicalBand)
            .status(true)
            .build());

    usersRolesRepository.save(new UsersRolesModel(role, musicalBand, user, true));

    // When
    UsersRolesModel usersRole = usersRolesRepository.findByUserIdAndMusicalBandId(user.getId(), musicalBand.getId())
        .orElseThrow(() -> new IllegalStateException("UsersRolesModel not found"));
    
    // Then
    assertNotNull(usersRole);
    assertEquals(usersRole.getUser().getId(), user.getId());
    assertEquals(usersRole.getMusicalBand().getId(), musicalBand.getId());
  }
}
