package com.bandsyncapi.bandsyncapi.api.v1.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bandsyncapi.bandsyncapi.api.v1.models.UsersRolesModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.RolesModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.UsersRolesKey;
import java.util.List;

/**
 * This interface is a repository for the users_roles table in the database.
 * Provides methods for performing CRUD operations on the users_roles table.
 */
@Repository
public interface UsersRolesRepository extends JpaRepository<UsersRolesModel, UsersRolesKey> {

  /**
   * Finds a UsersRolesModel by userId and musicalBandId.
   * 
   * @param userId        - the ID of the user
   * @param musicalBandId - the ID of the musical band
   * @return
   */
  @Query("""
      SELECT ur
      FROM UsersRolesModel ur
      JOIN FETCH ur.role r
      JOIN FETCH ur.musicalBand mb
      JOIN FETCH r.musicalBand
      JOIN FETCH ur.user u
      WHERE u.id = :userId AND mb.id = :musicalBandId
      """)
  Optional<UsersRolesModel> findByUserIdAndMusicalBandId(UUID userId, UUID musicalBandId);

  /**
   * finds users with a specific role
   * 
   * @param role - role
   * @return - List of UsersRolesModel
   */
  @Query("""
      SELECT ur
      FROM UsersRolesModel ur
      JOIN FETCH ur.role r
      JOIN FETCH ur.musicalBand mb
      JOIN FETCH r.musicalBand
      JOIN FETCH ur.user u
      WHERE r.id = :roleId
      """)
  List<UsersRolesModel> findByRole(@Param("roleId") Integer roleId);

  /**
   * Find all of a user's roles in the different bands they belong to
   * 
   * @param user - UsersModel
   * @return - All user roles
   */
  @Query("""
    SELECT ur
    FROM UsersRolesModel ur
    JOIN FETCH ur.role r
    JOIN FETCH ur.musicalBand
    JOIN FETCH r.musicalBand
    JOIN FETCH ur.user u
    WHERE ur.user.id = :userId
    """)
  List<UsersRolesModel> findByUser(@Param("userId") UUID userId);

  /**
   * find user's roles of a musical band
   * 
   * @param musicalBand
   * @return
   */
  @Query("""
    SELECT ur
    FROM UsersRolesModel ur
    JOIN FETCH ur.role r
    JOIN FETCH ur.musicalBand mb
    JOIN FETCH r.musicalBand
    JOIN FETCH ur.user u
    WHERE mb.id = :musicalBandId
    """)
  List<UsersRolesModel> findByMusicalBand(@Param("musicalBandId") UUID musicalBandId);

  /**
   * Assign role to a user in a musical band
   * 
   * @param roleId        - role id
   * @param userId        - user id
   * @param musicalBandId - musical band id
   */
  @Transactional
  @Query(value = """
      INSERT INTO users_roles (role_id, user_id, musical_band_id)
      VALUES (:roleId, :userId, :musicalBandId)
      """, nativeQuery = true)
  void assignRoleToUserInBand(@Param("roleId") Integer roleId, @Param("userId") UUID userId,
      @Param("musicalBandId") UUID musicalBandId);

  /**
   * Update role of a user in a musical band
   * 
   * @param role          - new role
   * @param userId        - user id
   * @param musicalBandId - musical band id
   * @return - row affected
   */
  @Transactional
  @Modifying
  @Query("""
      UPDATE UsersRolesModel ur
      SET ur.role = :role
      WHERE ur.user.id = :userId AND ur.musicalBand.id = :musicalBandId
      """)
  int updateUserRole(@Param("role") RolesModel role, @Param("userId") UUID userId,
      @Param("musicalBandId") UUID musicalBandId);

  /**
   * Delete a user from a musical band
   * 
   * @param userId        - UUID of the user
   * @param musicalBandId - UUID of the musical band
   */
  @Transactional
  @Modifying
  @Query(value = "DELETE FROM users_roles WHERE user_id = :userId AND musical_band_id = :musicalBandId", nativeQuery = true)
  void deleteByUserIdAndMusicalBandId(UUID userId, UUID musicalBandId);


  /**
   * delete by musical band id
   * 
   * @param musicalBandId - musical band id
   */
  @Transactional
  @Modifying
  @Query(value = "DELETE FROM users_roles WHERE musical_band_id = :musicalBandId", nativeQuery = true)
  void deleteByMusicalBandId(@Param("musicalBandId") UUID musicalBandId);
}
