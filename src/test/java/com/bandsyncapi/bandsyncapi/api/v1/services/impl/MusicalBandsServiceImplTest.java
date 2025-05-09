package com.bandsyncapi.bandsyncapi.api.v1.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bandsyncapi.bandsyncapi.api.v1.dto.musicalbands.MusicalBandsDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.musicalbands.MusicalBandsPostDto;
import com.bandsyncapi.bandsyncapi.api.v1.mappers.MusicalBandsMapper;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalBandsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.PermissionsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.RolesModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.RolesPermissionsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.UsersModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.UsersRolesModel;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.MusicalBandsRepository;
import com.bandsyncapi.bandsyncapi.api.v1.services.PermissionsService;
import com.bandsyncapi.bandsyncapi.api.v1.services.RolesPermissionsService;
import com.bandsyncapi.bandsyncapi.api.v1.services.RolesService;
import com.bandsyncapi.bandsyncapi.api.v1.services.UsersMusicalBandsService;
import com.bandsyncapi.bandsyncapi.api.v1.services.UsersRolesService;

@ExtendWith(MockitoExtension.class)
class MusicalBandsServiceImplTest {

  @Mock
  private MusicalBandsRepository musicalBandsRepository;

  @Mock
  private MusicalBandsMapper musicalBandsMapper;

  @Mock
  private UsersMusicalBandsService usersMusicalBandsService;

  @Mock
  private RolesService rolesService;

  @Mock
  private UsersRolesService usersRolesService;

  @Mock
  private PermissionsService permissionsService;

  @Mock
  private RolesPermissionsService rolesPermissionsService;

  @InjectMocks
  private MusicalBandsServiceImpl musicalBandsServiceImpl;

  @Test
  void testSaveMusicalBand() {
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

    // When
    musicalBandsServiceImpl.save(musicalBand);

    ArgumentCaptor<MusicalBandsModel> captor = ArgumentCaptor.forClass(MusicalBandsModel.class);

    verify(musicalBandsRepository).save(captor.capture());

    MusicalBandsModel musicalBandCaptured = captor.getValue();

    assertEquals(musicalBandCaptured, musicalBand);
  }

  @Test
  void testFindById() {
    // Given
    var musicalBandId = UUID.randomUUID();

    // When
    musicalBandsServiceImpl.findById(musicalBandId);

    // Then
    verify(musicalBandsRepository).findById(musicalBandId);
  }

  @Test
  void testRegisterMusicalBand () {
    // Given
    var musicalBandPostDto = new MusicalBandsPostDto(
      UUID.randomUUID(),
      "Test Musical Band", 
      "Test Logo", 
      "Test Address", 
      "8093459854", 
      "Test@Gamil.com", 
      true, 
      new UsersModel(UUID.randomUUID()));

    var musicalBand = MusicalBandsModel.builder()
      .id(UUID.randomUUID())
      .name("Test Band")
      .hyphenatedName("Test-band")
      .logo("test_logo.png")
      .address("Test Address")
      .phone("123456789")
      .email("testEmail@gmail.com")
      .status(true)
      .build();   
    
    var musicalBandDto = new MusicalBandsDto(
      UUID.randomUUID(), 
      "Test Band", 
      "test_logo.png", 
      "Test Address", 
      "123456789", 
      "testEmail@gmail.com", 
      true);

    var role = RolesModel.builder()
      .musicalBand(musicalBand)
      .name("Propietario")
      .status(true)
      .build();

    var permissions = List.of(new PermissionsModel(1, "PERMISSION_1", true));
    var rolesPermissions = permissions.stream()
        .map(permission -> new RolesPermissionsModel(role, permission, true))
        .toList();

    given(musicalBandsMapper.toModel(musicalBandPostDto)).willReturn(musicalBand);
    given(musicalBandsMapper.toDto(musicalBand)).willReturn(musicalBandDto);
    given(musicalBandsRepository.save(musicalBand)).willReturn(musicalBand);
    given(rolesService.save(role)).willReturn(role);
    given(permissionsService.findAll()).willReturn(permissions);

    // When
    MusicalBandsDto result = musicalBandsServiceImpl.registerMusicalBand(musicalBandPostDto);

    // Then
    verify(musicalBandsMapper).toModel(musicalBandPostDto);
    verify(musicalBandsRepository).save(musicalBand);
    verify(usersMusicalBandsService).save(musicalBandPostDto.user(), musicalBand);
    verify(rolesService).save(role);
    verify(usersRolesService).save(new UsersRolesModel(role, musicalBand, musicalBandPostDto.user(), true));
    verify(permissionsService).findAll();
    verify(rolesPermissionsService).saveAll(rolesPermissions);
    verify(musicalBandsMapper).toDto(musicalBand);

    assertNotNull(result);
    assertEquals(musicalBand.getName(), result.name());
    assertEquals(musicalBand.getLogo(), result.logo());
    assertEquals(musicalBand.getAddress(), result.address());
    assertEquals(musicalBand.getPhone(), result.phone());
    assertEquals(musicalBand.getEmail(), result.email());
    assertEquals(musicalBand.getStatus(), result.status());
  }

  @Test
  void testExistsById () {
    // Given
    var musicalBandId = UUID.randomUUID();

    // when
    musicalBandsServiceImpl.existsById(musicalBandId);

    // Then
    verify(musicalBandsRepository).existsById(musicalBandId);
  }
}