package com.bandsyncapi.bandsyncapi.api.v1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalRolesModel;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;


/*
 * This interface is a repository for the musical_roles table in the database.
 * Provides methods for performing CRUD operations on the musical_roles table.
 */
@Repository
public interface MusicalRolesRepository extends JpaRepository<MusicalRolesModel, Integer> {

  /**
   * finds musical roles by musical band id
   * 
   * @param id - musical band id
   * @return A MusicalRolesModel list
   */
  @Query("""
      SELECT mr FROM MusicalRolesModel mr 
      JOIN mr.musicalBand mb
      WHERE mb.id = :musicalBandId
      """)
  List<MusicalRolesModel> findByMusicalBandId(@Param("musicalBandId") UUID musicalBandId);

  /**
   * update musical role name
   * 
   * @param id - musical role id
   * @param musicalRoleName - new musical role name
   * @return - The row updated 
   */
  @Modifying
  @Transactional
  @Query("UPDATE MusicalRolesModel mr SET mr.name = :musicalRoleName WHERE mr.id = :id")
  int updateMusicalRoleName(@Param("id") Integer id ,@Param("musicalRoleName") String musicalRoleName);
}
