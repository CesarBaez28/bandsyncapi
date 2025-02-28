package com.bandsyncapi.bandsyncapi.api.v1.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalRolesUsersKey;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalRolesUsersModel;
import com.bandsyncapi.bandsyncapi.api.v1.projections.MusicalRolesUsersProjection;

/*
 * This interface is a repository for the musical_roles_users table in the database.
 * Provides methods for performing CRUD operations on the musical_roles_users table.
 */
public interface MusicalRolesUsersRepository extends JpaRepository<MusicalRolesUsersModel, MusicalRolesUsersKey> {

  /**
   * Find all musical roles of the users of a specific musical band
   * 
   * @param musicalBandId - Musical Band id
   * @return - A MusicalRolesUsersModel List
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
}
