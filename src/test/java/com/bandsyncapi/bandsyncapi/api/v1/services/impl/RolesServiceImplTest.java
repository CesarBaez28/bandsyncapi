package com.bandsyncapi.bandsyncapi.api.v1.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bandsyncapi.bandsyncapi.api.v1.dto.roles.RolesDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.roles.RolesPermissionsPutDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.roles.RolesPostDto;
import com.bandsyncapi.bandsyncapi.api.v1.mappers.RolesMapper;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalBandsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.PermissionsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.RolesModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.RolesPermissionsModel;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.RolesRepository;
import com.bandsyncapi.bandsyncapi.api.v1.services.RolesPermissionsService;

@ExtendWith(MockitoExtension.class)
class RolesServiceImplTest {

  @Mock
  private RolesRepository rolesRepository;

  @Mock
  private RolesPermissionsService rolesPermissionsService;

  @Mock
  private RolesMapper rolesMapper;

  @InjectMocks
  private RolesServiceImpl rolesServiceImpl;

  @Test
  void testFindAll() {
    // When
    rolesServiceImpl.findAll();

    // Then
    verify(rolesRepository).findAll();
  }

  @Test
  void testSave() {
    // Given
    var role = RolesModel.builder()
        .name("Test name")
        .musicalBand(new MusicalBandsModel(UUID.randomUUID()))
        .status(true)
        .build();

    // When
    rolesServiceImpl.save(role);

    // Then
    ArgumentCaptor<RolesModel> captor = ArgumentCaptor.forClass(RolesModel.class);

    verify(rolesRepository).save(captor.capture());

    RolesModel capturedRole = captor.getValue();

    assertEquals(role, capturedRole);
  }

  @Test
  void testSaveRoleAndPermissions() {
    // Given
    var musicalBandId = UUID.randomUUID();
    List<PermissionsModel> permissions = List.of(new PermissionsModel(1));
    var rolesPostDto = new RolesPostDto(
        "Test name",
        new MusicalBandsModel(musicalBandId),
        true,
        permissions);

    var role = RolesModel.builder()
        .id(1)
        .name("Test Role")
        .musicalBand(new MusicalBandsModel(musicalBandId))
        .status(true)
        .build();

    var roleDto = new RolesDto(1, "Test role", true);

    List<RolesPermissionsModel> rolesPermissions = rolesPostDto.permissions().stream()
        .map(permission -> new RolesPermissionsModel(role, permission, true)).toList();

    given(rolesMapper.toModel(rolesPostDto)).willReturn(role);
    given(rolesRepository.save(role)).willReturn(role);
    given(rolesMapper.toDto(role)).willReturn(roleDto);
    given(rolesPermissionsService.saveAll(anyList())).willReturn(rolesPermissions);

    // When
    rolesServiceImpl.saveRoleAndPermissions(rolesPostDto);

    // Then
    verify(rolesRepository).save(role);
    verify(rolesPermissionsService).saveAll(anyList());
  }

  @Test
  void testUpdateRolesPermissions() {
    // Given
    var musicalBandId = UUID.randomUUID();
    List<PermissionsModel> permissions = List.of(new PermissionsModel(1));
    List<PermissionsModel> actualPermissions = List.of(new PermissionsModel(2));
    var rolesPermissionsPutDto = new RolesPermissionsPutDto(
        1,
        "Test new name",
        permissions);

    var role = RolesModel.builder()
        .id(1)
        .name("Test Role")
        .musicalBand(new MusicalBandsModel(musicalBandId))
        .status(true)
        .build();

    List<RolesPermissionsModel> rolesPermissions = actualPermissions.stream()
        .map(permission -> new RolesPermissionsModel(role, permission, true)).toList();

    given(rolesRepository.findById(rolesPermissionsPutDto.roleId())).willReturn(Optional.of(role));
    given(rolesRepository.save(role)).willReturn(role);
    given(rolesPermissionsService.findAllByRole(role)).willReturn(rolesPermissions);

    Set<Integer> permissionsToDelete = Set.of(2);
    Set<Integer> permissionsToAdd = Set.of(1);

    List<RolesPermissionsModel> newPermissions = permissionsToAdd.stream()
        .map(id -> new RolesPermissionsModel(role, new PermissionsModel(id), true)).toList();

    // When
    rolesServiceImpl.updateRolesPermissions(rolesPermissionsPutDto);

    // Then
    verify(rolesPermissionsService).deleteByRoleIdAndPermissionIds(role.getId(), permissionsToDelete);
    verify(rolesPermissionsService).saveAll(newPermissions);
  }

  @Test
  void testUpdateRolesPermissionsWithNoPermissionsToDelete() {
    var musicalBandId = UUID.randomUUID();
    List<PermissionsModel> permissions = List.of(new PermissionsModel(1), new PermissionsModel(2));
    List<PermissionsModel> actualPermissions = List.of(new PermissionsModel(2));
    var rolesPermissionsPutDto = new RolesPermissionsPutDto(
        1,
        "Test new name",
        permissions);

    var role = RolesModel.builder()
        .id(1)
        .name("Test Role")
        .musicalBand(new MusicalBandsModel(musicalBandId))
        .status(true)
        .build();

    List<RolesPermissionsModel> rolesPermissions = actualPermissions.stream()
        .map(permission -> new RolesPermissionsModel(role, permission, true)).toList();

    given(rolesRepository.findById(rolesPermissionsPutDto.roleId())).willReturn(Optional.of(role));
    given(rolesRepository.save(role)).willReturn(role);
    given(rolesPermissionsService.findAllByRole(role)).willReturn(rolesPermissions);

    Set<Integer> permissionsToAdd = Set.of(1);

    List<RolesPermissionsModel> newPermissions = permissionsToAdd.stream()
        .map(id -> new RolesPermissionsModel(role, new PermissionsModel(id), true)).toList();

    // When
    rolesServiceImpl.updateRolesPermissions(rolesPermissionsPutDto);

    // Then
    verify(rolesPermissionsService, never()).deleteByRoleIdAndPermissionIds(anyInt(), anySet());
    verify(rolesPermissionsService).saveAll(newPermissions);
  }

  @Test
  void testUpdateRolesPermissionsWithNoPermissionsToAdd() {
    var musicalBandId = UUID.randomUUID();
    List<PermissionsModel> permissions = List.of(new PermissionsModel(1));
    List<PermissionsModel> actualPermissions = List.of(new PermissionsModel(1), new PermissionsModel(2));
    var rolesPermissionsPutDto = new RolesPermissionsPutDto(
        1,
        "Test new name",
        permissions);

    var role = RolesModel.builder()
        .id(1)
        .name("Test Role")
        .musicalBand(new MusicalBandsModel(musicalBandId))
        .status(true)
        .build();

    List<RolesPermissionsModel> rolesPermissions = actualPermissions.stream()
        .map(permission -> new RolesPermissionsModel(role, permission, true)).toList();

    given(rolesRepository.findById(rolesPermissionsPutDto.roleId())).willReturn(Optional.of(role));
    given(rolesRepository.save(role)).willReturn(role);
    given(rolesPermissionsService.findAllByRole(role)).willReturn(rolesPermissions);

    Set<Integer> permissionsToDelete = Set.of(2);

    // When
    rolesServiceImpl.updateRolesPermissions(rolesPermissionsPutDto);

    // Then
    verify(rolesPermissionsService).deleteByRoleIdAndPermissionIds(role.getId(), permissionsToDelete);
    verify(rolesPermissionsService, never()).saveAll(anyList());
  }
}
