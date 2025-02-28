package com.bandsyncapi.bandsyncapi.api.v1.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bandsyncapi.bandsyncapi.api.v1.models.UsersModel;

/*
 * This interface is a repository for the users table in the database.
 * Provides methods for performing CRUD operations on the users table.
 */
public interface UsersRepository extends JpaRepository<UsersModel, UUID>{

  @Query("""
      SELECT u FROM UsersModel u 
      JOIN UsersMusicalBandsModel um
      ON u.id = um.user.id
      WHERE um.musicalBand.id = :musicalBandId
      """)
  List<UsersModel> findAllByMusicalBandId (@Param("musicalBandId") UUID musicalBandId);
}
