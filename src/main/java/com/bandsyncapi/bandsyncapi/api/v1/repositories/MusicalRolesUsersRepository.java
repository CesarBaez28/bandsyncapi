package com.bandsyncapi.bandsyncapi.api.v1.repositories;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalRolesUsersKey;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalRolesUsersModel;
import com.bandsyncapi.bandsyncapi.api.v1.projections.MusicalRolesSingleUserProjection;
import com.bandsyncapi.bandsyncapi.api.v1.projections.MusicalRolesUsersProjection;

import jakarta.transaction.Transactional;

/*
 * This interface is a repository for the musical_roles_users table in the database.
 * Provides methods for performing CRUD operations on the musical_roles_users table.
 */
public interface MusicalRolesUsersRepository extends JpaRepository<MusicalRolesUsersModel, MusicalRolesUsersKey> {

  /**
   * Find all musical roles of the users of a specific musical band
   * 
   * @param musicalBandId - Musical Band id
   * @return - A MusicalRolesUsersProjection List
   */
  @Query("""
      SELECT
        mru.user.id AS userId,
        mru.musicalRole.id AS id,
        mru.musicalRole.name AS name,
        mru.musicalRole.status AS status
      FROM MusicalRolesUsersModel mru
      JOIN mru.user u
      JOIN mru.musicalBand mb
      WHERE mb.id = :musicalBandId
      """)
  List<MusicalRolesUsersProjection> findAllByMusicalBandId(@Param("musicalBandId") UUID musicalBandId);

  /**
   * finds musical roles of a specific user
   * 
   * @param musicalBandId - musical band id
   * @param userId        - user id
   * @return A MusicalRolesSingleUserProjection List
   */
  @Query("""
      SELECT
        mru.musicalRole.id AS id,
        mru.musicalRole.name AS name,
        mru.musicalRole.status AS status
      FROM MusicalRolesUsersModel mru
      JOIN mru.user u
      JOIN mru.musicalBand mb
      WHERE mb.id = :musicalBandId AND u.id = :userId
        """)
  List<MusicalRolesSingleUserProjection> findMusicalRolesUser(@Param("musicalBandId") UUID musicalBandId,
      @Param("userId") UUID userId);

  @Modifying
  @Transactional
  @Query("""
      DELETE FROM MusicalRolesUsersModel mru
      WHERE mru.user.id = :userId
      AND mru.musicalBand.id = :musicalBandId
      AND mru.musicalRole.id
      IN :roleIds
      """)
  void deleteByUserIdAndBandIdAndRoleIds(@Param("userId") UUID userId,
      @Param("musicalBandId") UUID musicalBandId,
      @Param("roleIds") Set<Integer> roleIds);

  /**
   * Delete by musical band id
   * 
   * @param musicalBandId - musical band id
   */
  @Modifying
  @Transactional
  @Query(value = "DELETE FROM musical_roles_users WHERE musical_band_id = :musicalBandId", nativeQuery = true)
  void deleteByMusicalBandId(@Param("musicalBandId") UUID musicalBandId);

  /**
   * Delete by user id
   * 
   * @param userId - user id
   */
  @Modifying
  @Transactional
  @Query(value = "DELETE FROM musical_roles_users WHERE user_id = :userId", nativeQuery = true)
  void deleteByUserId(@Param("userId") UUID userId);
}
