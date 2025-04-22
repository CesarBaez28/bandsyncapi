package com.bandsyncapi.bandsyncapi.api.v1.services.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalBandsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalRolesModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalRolesUsersModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.UsersModel;
import com.bandsyncapi.bandsyncapi.api.v1.projections.MusicalRolesSingleUserProjection;
import com.bandsyncapi.bandsyncapi.api.v1.projections.MusicalRolesUsersProjection;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.MusicalRolesUsersRepository;

@ExtendWith(MockitoExtension.class)
class MusicalRolesUsersServiceImplTest {

  @Mock
  MusicalRolesUsersRepository musicalRolesUsersRepository;

  @InjectMocks
  MusicalRolesUsersServiceImpl musicalRolesUsersServiceImpl;

  @Test
  void testFindAllByMusicalBandId() {
    // Given
    var musicalBandId = UUID.randomUUID();

    MusicalRolesUsersProjection actualRole = mock(MusicalRolesUsersProjection.class);
    List<MusicalRolesUsersProjection> list = List.of(actualRole);

    given(musicalRolesUsersRepository.findAllByMusicalBandId(musicalBandId)).willReturn(list);

    // When
    musicalRolesUsersServiceImpl.findAllByMusicalBandId(musicalBandId);

    // Then
    verify(musicalRolesUsersRepository).findAllByMusicalBandId(musicalBandId);
  }

  @Test
  void testFindAllByMusicalBandIdNotFound() {
    // Given
    var musicalBandId = UUID.randomUUID();

    given(musicalRolesUsersRepository.findAllByMusicalBandId(musicalBandId)).willReturn(anyList());

    // Then
    assertThrows(NoSuchElementException.class, () -> {
      musicalRolesUsersServiceImpl.findAllByMusicalBandId(musicalBandId);
    });
  }

  @Test
  void testFindMusicalRolesUser() {
    // Given
    var musicalBandId = UUID.randomUUID();
    var userId = UUID.randomUUID();

    // When
    musicalRolesUsersServiceImpl.findMusicalRolesUser(musicalBandId, userId);

    // Then
    verify(musicalRolesUsersRepository).findMusicalRolesUser(musicalBandId, userId);
  }

  @Test
  void testAssignMusicalRolesUser() {
    // Given
    var userId = UUID.randomUUID();
    var musicalBandId = UUID.randomUUID();
    var musicalRole = new MusicalRolesModel(1);

    List<MusicalRolesModel> rolesList = List.of(musicalRole);

    MusicalRolesSingleUserProjection actualRole = mock(MusicalRolesSingleUserProjection.class);
    given(actualRole.getId()).willReturn(2);

    List<MusicalRolesSingleUserProjection> lSingleUserProjections = List.of(actualRole);

    given(musicalRolesUsersServiceImpl.findMusicalRolesUser(musicalBandId, userId)).willReturn(lSingleUserProjections);

    Set<Integer> rolesToDelete = Set.of(2);

    List<MusicalRolesUsersModel> newRoles = List.of(
        new MusicalRolesUsersModel(
            musicalRole,
            new MusicalBandsModel(musicalBandId),
            new UsersModel(userId), true));

    // When
    musicalRolesUsersServiceImpl.assignMusicalRolesUser(userId, musicalBandId, rolesList);

    // Then
    verify(musicalRolesUsersRepository).deleteByUserIdAndBandIdAndRoleIds(userId, musicalBandId, rolesToDelete);
    verify(musicalRolesUsersRepository).saveAll(newRoles);
  }

  @Test
  void testAssignMusicalRolesUserNoRolesToDelete() {
    // Given
    var userId = UUID.randomUUID();
    var musicalBandId = UUID.randomUUID();
    var musicalRole = new MusicalRolesModel(1);
    var musicalRole2 = new MusicalRolesModel(2);

    List<MusicalRolesModel> rolesList = List.of(musicalRole, musicalRole2);

    MusicalRolesSingleUserProjection actualRole = mock(MusicalRolesSingleUserProjection.class);
    given(actualRole.getId()).willReturn(1);

    List<MusicalRolesSingleUserProjection> lSingleUserProjections = List.of(actualRole);

    given(musicalRolesUsersServiceImpl.findMusicalRolesUser(musicalBandId, userId)).willReturn(lSingleUserProjections);

    List<MusicalRolesUsersModel> newRoles = List.of(
        new MusicalRolesUsersModel(
            musicalRole2,
            new MusicalBandsModel(musicalBandId),
            new UsersModel(userId), true));

    // When
    musicalRolesUsersServiceImpl.assignMusicalRolesUser(userId, musicalBandId, rolesList);

    // Then
    verify(musicalRolesUsersRepository, never()).deleteByUserIdAndBandIdAndRoleIds(any(UUID.class), any(UUID.class),
        anySet());
    verify(musicalRolesUsersRepository).saveAll(newRoles);
  }

  @Test
  void testAssignMusicalRolesUserNoRolesToAdd() {
    // Given
    var userId = UUID.randomUUID();
    var musicalBandId = UUID.randomUUID();
    var musicalRole = new MusicalRolesModel(1);

    List<MusicalRolesModel> rolesList = List.of(musicalRole);

    MusicalRolesSingleUserProjection actualRole = mock(MusicalRolesSingleUserProjection.class);
    MusicalRolesSingleUserProjection actualRole2 = mock(MusicalRolesSingleUserProjection.class);
    given(actualRole.getId()).willReturn(1);
    given(actualRole2.getId()).willReturn(2);

    List<MusicalRolesSingleUserProjection> lSingleUserProjections = List.of(actualRole, actualRole2);

    given(musicalRolesUsersServiceImpl.findMusicalRolesUser(musicalBandId, userId)).willReturn(lSingleUserProjections);

    Set<Integer> rolesToDelete = Set.of(2);

    // When
    musicalRolesUsersServiceImpl.assignMusicalRolesUser(userId, musicalBandId, rolesList);

    // Then
    verify(musicalRolesUsersRepository).deleteByUserIdAndBandIdAndRoleIds(userId, musicalBandId, rolesToDelete);
    verify(musicalRolesUsersRepository, never()).saveAll(anyCollection());
  }
}
