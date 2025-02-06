package com.bandsyncapi.bandsyncapi.api.v1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalGenresModel;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;


/*
 * This interface is a repository for the musical_genres table in the database.
 * Provides methods for performing CRUD operations on the musical_genres table.
 */
@Repository
public interface MusicalGenresRepository extends JpaRepository<MusicalGenresModel, Integer> {

  /**
   * Get all musical genres by Musical Band id
   * @param musicalBandId - Musical Band id
   * @return - A list of musical genres
   */
  @Query(""" 
    SELECT mr FROM MusicalGenresModel mr 
    JOIN mr.musicalBand mb
    WHERE mb.id = :musicalBandId
  """)
  List<MusicalGenresModel> findByMusicalBandId(@Param("musicalBandId") UUID musicalBandId);

  /**
   * Update musical genre name
   * @param id - musical genre id
   * @param name - new musical genre name
   * @return - An integer number that represents the row updated
   */
  @Modifying
  @Transactional
  @Query("UPDATE MusicalGenresModel mg SET mg.name = :name WHERE mg.id = :id")
  int updateGenreName(@Param("id") Integer id, @Param("name") String name);
}
