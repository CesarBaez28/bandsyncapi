package com.bandsyncapi.bandsyncapi.api.v1.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalBandsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalRolesModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalRolesUsersModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.UsersModel;
import com.bandsyncapi.bandsyncapi.api.v1.projections.MusicalRolesSingleUserProjection;
import com.bandsyncapi.bandsyncapi.api.v1.projections.MusicalRolesUsersProjection;

@DataJpaTest
class MusicalRolesUsersRepositoryTest {

  @Autowired
  private MusicalRolesUsersRepository musicalRolesUsersRepository;

  @Autowired
  private MusicalBandsRepository musicalBandsRepository;

  @Autowired
  private UsersRepository usersRepository;

  @Autowired
  private MusicalRolesRepository musicalRolesRepository;

  @Test
  void testFindAllByMusicalBandId() {

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

    var musicalRole = MusicalRolesModel.builder()
        .name("Test Role")
        .musicalBand(musicalBand)
        .status(true)
        .build();
    var savedMusicalRole = musicalRolesRepository.save(musicalRole);

    var user = UsersModel.builder()
        .username("testUsername")
        .firstName("testFirstName")
        .lastName("testLastName")
        .email("test@gmail.com")
        .password("testPassword")
        .phone("testPhone")
        .photo("testPhoto")
        .status(true)
        .build();
    var savedUser = usersRepository.save(user);

    musicalRolesUsersRepository.save(
        new MusicalRolesUsersModel(savedMusicalRole, savedMusicalBand, savedUser, true));

    // When
    List<MusicalRolesUsersProjection> musicalRolesUsers = musicalRolesUsersRepository
        .findAllByMusicalBandId(savedMusicalBand.getId());

    // Then
    assertNotNull(musicalRolesUsers);
    assertFalse(musicalRolesUsers.isEmpty());
    assertEquals(savedMusicalRole.getName(), musicalRolesUsers.get(0).getName());
  }

  @Test
  void testFindMusicalRolesUser() {
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

    var musicalRole = MusicalRolesModel.builder()
        .name("Test Role")
        .musicalBand(musicalBand)
        .status(true)
        .build();
    var savedMusicalRole = musicalRolesRepository.save(musicalRole);

    var user = UsersModel.builder()
        .username("testUsername")
        .firstName("testFirstName")
        .lastName("testLastName")
        .email("test@gmail.com")
        .password("testPassword")
        .phone("testPhone")
        .photo("testPhoto")
        .status(true)
        .build();
    var savedUser = usersRepository.save(user);

    musicalRolesUsersRepository.save(
        new MusicalRolesUsersModel(savedMusicalRole, savedMusicalBand, savedUser, true));

    // When
    List<MusicalRolesSingleUserProjection> musicalRolesUsers = musicalRolesUsersRepository
        .findMusicalRolesUser(savedMusicalBand.getId(), savedUser.getId());

    // Then
    assertNotNull(musicalRolesUsers);
    assertFalse(musicalRolesUsers.isEmpty());
    assertEquals(savedMusicalRole.getName(), musicalRolesUsers.get(0).getName());
  }

  @Test
  void testDeleteByUserIdAndBandIdAndRoleIds() {

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

    var musicalRole = MusicalRolesModel.builder()
        .name("Test Role")
        .musicalBand(musicalBand)
        .status(true)
        .build();
    var savedMusicalRole = musicalRolesRepository.save(musicalRole);

    var user = UsersModel.builder()
        .username("testUsername")
        .firstName("testFirstName")
        .lastName("testLastName")
        .email("test@gmail.com")
        .password("testPassword")
        .phone("testPhone")
        .photo("testPhoto")
        .status(true)
        .build();
    var savedUser = usersRepository.save(user);

    musicalRolesUsersRepository.save(
        new MusicalRolesUsersModel(savedMusicalRole, savedMusicalBand, savedUser, true));

    // When
    Set<Integer> roleIds = Set.of(savedMusicalRole.getId());
    musicalRolesUsersRepository.deleteByUserIdAndBandIdAndRoleIds(savedUser.getId(), savedMusicalBand.getId(), roleIds);

    List<MusicalRolesSingleUserProjection> musicalRolesUsers = musicalRolesUsersRepository
        .findMusicalRolesUser(savedMusicalBand.getId(), savedUser.getId());

    // Then
    assertTrue(musicalRolesUsers.isEmpty());
  }
}
