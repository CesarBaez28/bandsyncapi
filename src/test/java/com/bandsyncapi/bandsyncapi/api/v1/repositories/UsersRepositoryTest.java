package com.bandsyncapi.bandsyncapi.api.v1.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.bandsyncapi.bandsyncapi.api.v1.dto.users.UsersPutDto;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalBandsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.UsersModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.UsersMusicalBandsModel;

@DataJpaTest
class UsersRepositoryTest {

  @Autowired
  private UsersRepository usersRepository;

  @Autowired
  private MusicalBandsRepository musicalBandsRepository;

  @Autowired
  private UsersMusicalBandsRepository usersMusicalBandsRepository;

  @Test
  void testFindAllByMusicalBandId() {
    // Given

    var musicalBand = musicalBandsRepository.save(
        MusicalBandsModel.builder()
            .name("Test Band")
            .hyphenatedName("Test-band")
            .logo("test_logo.png")
            .address("Test Address")
            .phone("123456789")
            .email("test@gmail.com")
            .status(true)
            .build());

    var user = usersRepository.save(
        UsersModel.builder()
            .username("test_user")
            .email("test@gmail.com")
            .password("testPassword")
            .firstName("test first name")
            .lastName("test last name")
            .phone("test phone")
            .photo("test_photo.png")
            .status(true)
            .build());

    usersMusicalBandsRepository.save(new UsersMusicalBandsModel(user, musicalBand, true));

    // When
    List<UsersModel> users = usersRepository.findAllByMusicalBandId(musicalBand.getId());

    // Then
    assertNotNull(users);
    assertFalse(users.isEmpty());
  }

  @Test
  void updateUser() {
    // Given
    var user = usersRepository.save(
        UsersModel.builder()
            .username("test_user")
            .email("test@gmail.com")
            .password("testPassword")
            .firstName("test first name")
            .lastName("test last name")
            .phone("test phone")
            .photo("test_photo.png")
            .status(true)
            .build());

    // When
    UsersPutDto updateUserDTO = new UsersPutDto(
        "updated first name",
        "updated last name",
        "8092341234",
        "UpdatedPhoto");

    int updatedRows = usersRepository.updateUser(user.getId(), updateUserDTO);

    // Then
    assertEquals(1, updatedRows);
  }

  @Test
  void testExistsByEmail() {
    // Given
    var user = usersRepository.save(
        UsersModel.builder()
            .username("test_user")
            .email("test@gmail.com")
            .password("testPassword")
            .firstName("test first name")
            .lastName("test last name")
            .phone("test phone")
            .photo("test_photo.png")
            .status(true)
            .build());

    // When
    boolean exists = usersRepository.existsByEmail(user.getEmail());

    // Then
    assertEquals(true, exists);
  }

  @Test
  void testNotExistsByEmail() {
    // Given
    String email = "non_existent_email";

    // When
    boolean exists = usersRepository.existsByEmail(email);

    // Then
    assertEquals(false, exists);
  }

  @Test
  void testFindByUserName() throws UsernameNotFoundException {
    // Given
    var user = usersRepository.save(
        UsersModel.builder()
            .username("test_user")
            .email("test@gmail.com")
            .password("testPassword")
            .firstName("test first name")
            .lastName("test last name")
            .phone("test phone")
            .photo("test_photo.png")
            .status(true)
            .build());

    // When
    UsersModel foundUser = usersRepository.findByUsername(user.getUsername())
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    // Then
    assertNotNull(foundUser);
    assertEquals(user.getUsername(), foundUser.getUsername());
  }

  @Test
  void testFindByUserNameNotFound() {
    // Given
    String username = "non_existent_user";

    // When
    Optional<UsersModel> foundUser = usersRepository.findByUsername(username);

    // Then
    assertFalse(foundUser.isPresent());
  }
}
