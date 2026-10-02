package com.bandsyncapi.bandsyncapi.api.v1.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import com.bandsyncapi.bandsyncapi.api.v1.dto.users.UserLoginPostDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.users.UsersPutDto;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalBandsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.UsersModel;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.UsersRepository;
import com.bandsyncapi.bandsyncapi.api.v1.services.FilesService;
import com.bandsyncapi.bandsyncapi.api.v1.services.UsersMusicalBandsService;
import com.bandsyncapi.bandsyncapi.utils.Encrypt;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class UsersServiceImplTest {

  private static final String USERS_DIRECTORY = "/users";

  @Mock
  private UsersRepository usersRepository;

  @Mock
  private UsersMusicalBandsService usersMusicalBandsService;

  @Mock
  private AuthenticationManager authenticationManager;

  @Mock
  private Encrypt encrypt;

  @Mock
  private FilesService filesService;

  @InjectMocks
  private UsersServiceImpl usersServiceImpl;

  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    this.objectMapper = new ObjectMapper();
    ReflectionTestUtils.setField(usersServiceImpl, "awsUsersDirectory", USERS_DIRECTORY);
  }

  @Test
  void testVerify() {
    // Given
    var userLoginPostDto = new UserLoginPostDto("username", "password");

    Authentication authenticationMock = mock(Authentication.class);
    given(authenticationMock.isAuthenticated()).willReturn(true);

    given(authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(userLoginPostDto.username(), userLoginPostDto.password())))
        .willReturn(authenticationMock);

    // When
    boolean isAuthenticated = usersServiceImpl.verify(userLoginPostDto);

    // Then
    verify(authenticationManager).authenticate(
        new UsernamePasswordAuthenticationToken(userLoginPostDto.username(), userLoginPostDto.password()));

    assertTrue(isAuthenticated);
  }

  @Test
  void testRegister() {
    // Given
    var userModel = UsersModel.builder()
        .id(UUID.randomUUID())
        .username("username")
        .password("password")
        .email("email")
        .firstName("firstName")
        .lastName("lastName")
        .phone("phone")
        .photo("photo")
        .status(true)
        .build();

    // When
    usersServiceImpl.register(userModel);

    // Then
    ArgumentCaptor<UsersModel> userModelCaptor = ArgumentCaptor.forClass(UsersModel.class);

    verify(usersRepository).save(userModelCaptor.capture());

    UsersModel savedUserModel = userModelCaptor.getValue();

    assertEquals(userModel, savedUserModel);
  }

  @Test
  void testJoinUserToMusicalBand() {
    // Given
    UUID userId = UUID.randomUUID();
    UUID musicalBandId = UUID.randomUUID();

    // When
    usersServiceImpl.joinUserToMusicalBand(userId, musicalBandId);

    // Then
    verify(usersMusicalBandsService).save(new UsersModel(userId), new MusicalBandsModel(musicalBandId));
  }

  @Test
  void testExistsByEmail() {
    // Given
    String email = "user@gmail.com";

    given(usersRepository.existsByEmail(email)).willReturn(true);
    // When

    boolean exists = usersServiceImpl.existsByEmail(email);

    // Then
    verify(usersRepository).existsByEmail(email);
    assertTrue(exists);
  }

  @Test
  void testGetAllUsersByMusicalBandId() {
    // Given
    UUID musicalBandId = UUID.randomUUID();

    given(usersRepository.findAllByMusicalBandId(musicalBandId)).willReturn(List.of(new UsersModel()));

    // When
    usersServiceImpl.getAllUsersByMusicalBandId(musicalBandId);

    // Then
    verify(usersRepository).findAllByMusicalBandId(musicalBandId);
  }

  @Test
  void testGetById() {
    // Given
    UUID userId = UUID.randomUUID();
    UsersModel userModel = new UsersModel(userId);

    given(usersRepository.findById(userId)).willReturn(java.util.Optional.of(userModel));

    // When
    usersServiceImpl.getById(userId);

    // Then
    verify(usersRepository).findById(userId);
  }

  @Test
  void testGetById_Empty() {
    // Given
    UUID userId = UUID.randomUUID();

    given(usersRepository.findById(userId)).willReturn(java.util.Optional.empty());

    // When
    assertThrows(EntityNotFoundException.class, () -> {
      usersServiceImpl.getById(userId);
    });

    // Then
    verify(usersRepository).findById(userId);
  }

  @Test
  void testUpdateUser() throws Exception {
    // Given
    var userId = UUID.randomUUID();
    var usersPutDto = new UsersPutDto(
        "firstName",
        "lastName",
        "phone",
        "correo@gmail.com",
        "photo");

    MockMultipartFile imageFile = new MockMultipartFile(
        "user",
        "",
        "application/json",
        objectMapper.writeValueAsString(usersPutDto).getBytes());

    given(usersRepository.updateUser(userId, usersPutDto)).willReturn(1);
    given(filesService.uploadFile(imageFile, USERS_DIRECTORY)).willReturn("user");

    // When
    usersServiceImpl.updateUser(userId, usersPutDto, imageFile);

    // Then
    verify(usersRepository).updateUser(userId, usersPutDto);
  }

  @Test
  void testUpdateUser_Empty() throws Exception {
    // Given
    var userId = UUID.randomUUID();
    var usersPutDto = new UsersPutDto(
        "firstName",
        "lastName",
        "phone",
        "correo@gmail.com",
        "photo");

    MockMultipartFile imageFile = new MockMultipartFile(
        "user",
        "",
        "application/json",
        objectMapper.writeValueAsString(usersPutDto).getBytes());

    given(usersRepository.updateUser(userId, usersPutDto)).willReturn(0);
    given(filesService.uploadFile(imageFile, USERS_DIRECTORY)).willReturn("user");

    // When
    assertThrows(EntityNotFoundException.class, () -> {
      usersServiceImpl.updateUser(userId, usersPutDto, imageFile);
    });

    // Then
    verify(usersRepository).updateUser(userId, usersPutDto);
  }
}
