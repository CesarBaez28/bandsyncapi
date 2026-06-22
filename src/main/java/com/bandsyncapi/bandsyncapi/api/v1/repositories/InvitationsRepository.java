package com.bandsyncapi.bandsyncapi.api.v1.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bandsyncapi.bandsyncapi.api.v1.models.InvitationsModel;
import java.util.Optional;

/**
 * Repository for the InvitationsModel to manage CRUD operations
 */
@Repository
public interface InvitationsRepository extends JpaRepository<InvitationsModel, UUID> {

  /**
   * find an invitation by token
   * 
   * @param token - token
   * @return - Optional of type InvitationsModel
   */
  Optional<InvitationsModel> findByToken(String token);

  /**
   * Deletes invitation by musical band ids
   * 
   * @param musicalBandId - musical band id
   */
  @Modifying
  @Transactional
  @Query(value = "DELETE FROM invitations WHERE musical_band_id = :musicalBandId", nativeQuery = true)
  void deleteByMusicalBandId(@Param("musicalBandId") UUID musicalBandId);

  /**
   * Deletes invitations by user id
   * 
   * @param userId - user id
   */
  @Modifying
  @Transactional
  @Query(value = "DELETE FROM invitations WHERE invited_by = :userId", nativeQuery = true)
  void deleteByUserId(@Param("userId") UUID userId);
}
