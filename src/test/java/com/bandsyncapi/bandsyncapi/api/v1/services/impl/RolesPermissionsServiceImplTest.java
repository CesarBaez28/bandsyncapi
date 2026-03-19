package com.bandsyncapi.bandsyncapi.api.v1.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalBandsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.PermissionsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.RolesModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.RolesPermissionsModel;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.RolesPermissionsRepository;

@ExtendWith(MockitoExtension.class)
class RolesPermissionsServiceImplTest {

  @Mock
  private RolesPermissionsRepository rolesPermissionsRepository;

  @InjectMocks
  private RolesPermissionsServiceImpl rolesPermissionsServiceImpl;

  @Test
  void testFindAllByRole() {
    // Given
    var role = RolesModel.builder()
        .id(1)
        .name("Test name")
        .musicalBand(new MusicalBandsModel(UUID.randomUUID()))
        .status(true)
        .build();

    // When
    rolesPermissionsServiceImpl.findAllByRole(role);

    // Then
    verify(rolesPermissionsRepository).findAllByRole(role.getId());
  }

  @Test
  void testSave() {
    // Given
    var role = new RolesModel(1);
    var permissions = new PermissionsModel(1);
    var rolesPermissions = new RolesPermissionsModel(role, permissions, true);

    // When
    rolesPermissionsServiceImpl.save(rolesPermissions);

    // Then
    ArgumentCaptor<RolesPermissionsModel> captor = ArgumentCaptor.forClass(RolesPermissionsModel.class);

    verify(rolesPermissionsRepository).save(captor.capture());

    RolesPermissionsModel captured = captor.getValue();

    assertEquals(rolesPermissions, captured);
  }

  @Test
  void testSaveAll() {
    // Given
    var role = new RolesModel(1);
    var permissions = new PermissionsModel(1);
    var rolesPermissions = new RolesPermissionsModel(role, permissions, true);

    List<RolesPermissionsModel> list = List.of(rolesPermissions);

    // When
    rolesPermissionsServiceImpl.saveAll(list);

    // Then
    verify(rolesPermissionsRepository).saveAll(list);
  }

  @Test
  void TestDeleteByRoleIdAndPermissionIds() {
    // Given
    Integer roleId = 1;
    Set<Integer> permissionsId = Set.of(1, 2, 3);

    // When
    rolesPermissionsRepository.deleteByRoleIdAndPermissionIdIn(roleId, permissionsId);

    // Then
    verify(rolesPermissionsRepository).deleteByRoleIdAndPermissionIdIn(roleId, permissionsId);
  }
}
