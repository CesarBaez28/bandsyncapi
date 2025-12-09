package com.bandsyncapi.bandsyncapi.api.v1.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bandsyncapi.bandsyncapi.api.v1.models.ArtistsModel;

import jakarta.transaction.Transactional;

/**
 * This interface is repository for the artists table in the database.
 * Provides methods for performing CRUD operations on the artists table.
 */
@Repository
public interface ArtistsRepository extends JpaRepository<ArtistsModel, Integer> {

  /**
   * Get all Artists by Musical Band id
   * 
   * @param musicalBandId - Musical Band id
   * @return - A list of Artists
   */
  @Query("""
        SELECT art FROM ArtistsModel art
        JOIN art.musicalBand mb
        WHERE mb.id = :musicalBandId
      """)
  List<ArtistsModel> findByMusicalBandId(@Param("musicalBandId") UUID musicalBandId);

  /**
   * Get all Artists by Musical Band id and name
   * 
   * @param musicalBandId - Musical Band id
   * @param name          - Artists name
   * @return - A list of Artists
   */
  @Query("""
        SELECT art FROM ArtistsModel art
        JOIN art.musicalBand mb
        WHERE mb.id = :musicalBandId AND art.name LIKE %:name%
      """)
  Page<ArtistsModel> findByMusicalBandIdAndName(@Param("musicalBandId") UUID musicalBandId, @Param("name") String name,
      Pageable pageable);

  /**
   * Update Artists name
   * 
   * @param id   - Artists id
   * @param name - new Artists name
   * @return - An integer number that represents the row updated
   */
  @Modifying
  @Transactional
  @Query("UPDATE ArtistsModel art SET art.name = :name WHERE art.id = :id")
  int updateArtistName(@Param("id") Integer id, @Param("name") String name);

  /**
   * Delete artist by id
   * 
   * @param id - Artist id
   */
  @Transactional
  @Modifying
  @Query(value = "DELETE FROM artists WHERE id = :id", nativeQuery = true)
  void deleteArtistById(@Param("id") Integer id);

  /**
   * Deletes by musical band id
   * 
   * @param musicalBandId - musical band id
   */
  @Transactional
  @Modifying
  @Query(value = "DELETE FROM artists WHERE musical_band_id = :musicalBandId", nativeQuery = true)
  void deleteByMusicalBandId(@Param("musicalBandId") UUID musicalBandId);
}
