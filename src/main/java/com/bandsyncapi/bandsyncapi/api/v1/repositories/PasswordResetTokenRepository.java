package com.bandsyncapi.bandsyncapi.api.v1.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bandsyncapi.bandsyncapi.api.v1.models.PasswordResetTokenModel;

/**
 * Repository for the PasswordResetTokenModel
 */
@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetTokenModel, UUID> {

  /**
   * Find a password reset token by its token value
   * 
   * @param token - The token value to search for
   * @return - An Optional containing the found PasswordResetTokenModel, or empty if not found
   */
  Optional<PasswordResetTokenModel> findByToken(UUID token);

  /**
   * Delete a password reset token by its user ID
   * 
   * @param userId - The user ID for which to delete reset tokens
   */
  @Transactional
  @Modifying
  @Query(value = "DELETE FROM password_reset_tokens WHERE user_id = :userId", nativeQuery = true)
  void deleteByUserId(UUID userId);
}
