package com.bandsyncapi.bandsyncapi.api.v1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.bandsyncapi.bandsyncapi.api.v1.models.UsersMusicalBandsKey;
import com.bandsyncapi.bandsyncapi.api.v1.models.UsersMusicalBandsModel;
import java.util.List;
import java.util.UUID;

/*
 * This interface is a repository for the users_musical_bands table in the database.
 * Provides methods for performing CRUD operations on the users_musical_bands table.
 */
public interface UsersMusicalBandsRepository extends JpaRepository<UsersMusicalBandsModel, UsersMusicalBandsKey> {

  /**
   * Find all the bands a user is a part of
   * 
   * @param user - UsersModel object
   * @return - A List of UsersMusicalBandsModel
   */
  @Query("""
      SELECT umb FROM UsersMusicalBandsModel umb
      JOIN FETCH umb.musicalBand mb
      JOIN FETCH umb.user u
      WHERE u.id = :userId
        """)
  List<UsersMusicalBandsModel> findByUser(@Param("userId") UUID userId);

  /**
   * Delete a user from a musical band
   * 
   * @param userId        - UUID of the user
   * @param musicalBandId - UUID of the musical band
   */
  @Transactional
  @Modifying
  @Query("DELETE FROM UsersMusicalBandsModel umb WHERE umb.user.id = :userId AND umb.musicalBand.id = :musicalBandId")
  void deleteByUserIdAndMusicalBandId(UUID userId, UUID musicalBandId);

  /**
   * Delete by musical band id
   * 
   * @param musicalBandId - musical band id
   */
  @Transactional
  @Modifying
  @Query(value = "DELETE FROM users_musical_bands WHERE musical_band_id = :musicalBandId", nativeQuery = true)
  void deleteByMusicalBandId(@Param("musicalBandId") UUID musicalBandId);
}
