package com.bandsyncapi.bandsyncapi.api.v1.repositories;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalBandsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.PermissionsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.RolesModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.RolesPermissionsModel;

@DataJpaTest
class RolesPermissionsRepositoryTest {

  @Autowired
  private PermissionsRepository permissionsRepository;

  @Autowired
  private RolesPermissionsRepository rolesPermissionsRepository;

  @Autowired
  private RolesRepository rolesRepository;

  @Autowired
  private MusicalBandsRepository musicalBandsRepository;

  @Test
  void testFindAllByRole() {
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

    var permission = permissionsRepository.save(
        PermissionsModel.builder()
            .name("Permission Test")
            .status(true)
            .build());

    rolesPermissionsRepository.save(new RolesPermissionsModel(savedRole, permission, true));

    // When
    List<RolesPermissionsModel> rolesPermissionsList = rolesPermissionsRepository.findAllByRole(savedRole);

    // Then
    assertNotNull(rolesPermissionsList);
    assertFalse(rolesPermissionsList.isEmpty());
  }

  @Test
  void testDeleteByRoleIdAndPermissionIdIn() {
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

    var permission = permissionsRepository.save(
        PermissionsModel.builder()
            .name("Permission Test")
            .status(true)
            .build());

    rolesPermissionsRepository.save(new RolesPermissionsModel(savedRole, permission, true));

    // When
    rolesPermissionsRepository.deleteByRoleIdAndPermissionIdIn(savedRole.getId(), Set.of(permission.getId())); 

    List<RolesPermissionsModel> rolesPermissionsList = rolesPermissionsRepository.findAllByRole(savedRole);

    // Then
    assertTrue(rolesPermissionsList.isEmpty());
  }

}
