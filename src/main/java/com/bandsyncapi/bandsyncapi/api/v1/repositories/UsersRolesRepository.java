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
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalBandsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.RolesModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.UsersModel;
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
  Optional<UsersRolesModel> findByUserIdAndMusicalBandId(UUID userId, UUID musicalBandId);

  /**
   * finds users with a specific role
   * 
   * @param role - role
   * @return - List of UsersRolesModel
   */
  List<UsersRolesModel> findByRole(RolesModel role);

  /**
   * Find all of a user's roles in the different bands they belong to
   * 
   * @param user - UsersModel
   * @return - All user roles
   */
  List<UsersRolesModel> findByUser(UsersModel user);

  /**
   * find user's roles of a musical band
   * 
   * @param musicalBand
   * @return
   */
  List<UsersRolesModel> findByMusicalBand(MusicalBandsModel musicalBand);

  /**
   * Update role of a user in a musical band
   * 
   * @param role - new role
   * @param userId - user id
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
  int updateUserRolec(@Param("role") RolesModel role, @Param("userId") UUID userId,
      @Param("musicalBandId") UUID musicalBandId);


  /**
   * Delete a user from a musical band
   * 
   * @param userId - UUID of the user
   * @param musicalBandId - UUID of the musical band
   */    
  @Transactional
  @Modifying
  @Query("DELETE FROM UsersRolesModel ur WHERE ur.user.id = :userId AND ur.musicalBand.id = :musicalBandId")
  void deleteByUserIdAndMusicalBandId(UUID userId, UUID musicalBandId);
}
