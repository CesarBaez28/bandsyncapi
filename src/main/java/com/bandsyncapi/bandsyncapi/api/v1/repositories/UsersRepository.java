package com.bandsyncapi.bandsyncapi.api.v1.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bandsyncapi.bandsyncapi.api.v1.dto.users.UsersPutDto;
import com.bandsyncapi.bandsyncapi.api.v1.models.UsersModel;
import jakarta.transaction.Transactional;

/*
 * This interface is a repository for the users table in the database.
 * Provides methods for performing CRUD operations on the users table.
 */
public interface UsersRepository extends JpaRepository<UsersModel, UUID> {

  /**
   * find all users that are part of a musical band
   * 
   * @param musicalBandId - Musical band id
   * @return - A UsersModel List
   */
  @Query("""
      SELECT u FROM UsersModel u
      JOIN UsersMusicalBandsModel um
      ON u.id = um.user.id
      WHERE um.musicalBand.id = :musicalBandId
      """)
  List<UsersModel> findAllByMusicalBandId(@Param("musicalBandId") UUID musicalBandId);

  /**
   * find all users that are part of a musical band and by
   * username, email, firstname, lastanme and phone number
   * 
   * @param musicalBandId - musical band id
   * @param term          - search term
   * @param pageable      - Pageable for pagination
   * @return Page of UsersModel
   */
  @Query("""
      SELECT u FROM UsersModel u
      JOIN UsersMusicalBandsModel um
      ON u.id = um.user.id
      WHERE um.musicalBand.id = :musicalBandId AND
      (
        u.username LIKE %:term% OR
        u.email LIKE %:term% OR
        u.firstName LIKE %:term% OR
        u.lastName LIKE %:term% OR
        u.phone LIKE %:term%
      )
      """)
  Page<UsersModel> find(@Param("musicalBandId") UUID musicalBandId,
      @Param("term") String term, Pageable pageable);

  /**
   * Update user info
   * 
   * @param updateUserDTO - user info to be updated
   * @return
   */
  @Transactional
  @Modifying
  @Query("""
      UPDATE UsersModel u SET
        u.firstName = :#{#updateUserDTO.firstName},
        u.lastName = :#{#updateUserDTO.lastName},
        u.phone = :#{#updateUserDTO.phone},
        u.email = :#{#updateUserDTO.email},
        u.photo = :#{#updateUserDTO.photo}
      WHERE u.id = :userId
      """)
  int updateUser(@Param("userId") UUID userId, @Param("updateUserDTO") UsersPutDto updateUserDTO);

  /**
   * Check if the user exists by email
   * 
   * @param email - email
   * @return boolean
   */
  boolean existsByEmail(String email);

  /**
   * get user by username
   * 
   * @param username - username
   * @return - UsersModel object
   */
  Optional<UsersModel> findByUsername(String username);
}
