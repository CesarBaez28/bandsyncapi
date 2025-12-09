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

import com.bandsyncapi.bandsyncapi.api.v1.models.RepertoiresModel;

import jakarta.transaction.Transactional;

/*
 * This interface is a repository for the repertoires table in the database.
 * Provides methods for performing CRUD operations on the repertoires table.
 */
@Repository
public interface RepertoiresRepository extends JpaRepository<RepertoiresModel, UUID> {

  /**
   * finds repertoires by musical band id
   * 
   * @param musicalBandId - musical band id
   * @return - RepertoiresModel list
   */
  @Query("""
      SELECT rp FROM RepertoiresModel rp
      JOIN rp.musicalBand mb
      WHERE mb.id = :musicalBandId
        """)
  List<RepertoiresModel> findByMusicalBandId(@Param("musicalBandId") UUID musicalBandId);

  /**
   * finds repertoires by musical band id, name and description
   * 
   * @param musicalBandId - musical band id
   * @param term          - search term
   * @param pageable      - Pageable object for pagination
   * @return - Page of RepertoiresModel
   */
  @Query("""
      SELECT rp FROM RepertoiresModel rp
      JOIN rp.musicalBand mb
      WHERE mb.id = :musicalBandId AND
      (
        rp.name LIKE %:term% OR
        rp.description LIKE %:term%
      )
      """)
  Page<RepertoiresModel> find(@Param("musicalBandId") UUID musicalBandId,
      @Param("term") String term, Pageable pageable);

  /**
   * update repertoire info
   * 
   * @param id          - repertoire id
   * @param name        - repertoire name
   * @param description - repertoire description
   * @param link        - repertoire link
   * @param status      - repertoire status
   * @return - An integer number that represents the row updated
   */
  @Transactional
  @Modifying
  @Query("""
      UPDATE RepertoiresModel rp set rp.name = :name, rp.description = :description, rp.link = :link, rp.status = :status WHERE rp.id = :id
      """)
  int updateRepertoire(@Param("id") UUID id, @Param("name") String name,
      @Param("description") String description, @Param("link") String link, @Param("status") Boolean status);

  /**
   * Delete repertoires by musical band id
   * 
   * @param musicalBandId - musical band id
   */    
  @Transactional
  @Modifying
  @Query(value = "DELETE FROM repertoires WHERE musical_band_id = :musicalBandId", nativeQuery = true)
  void deleteByMusicalBandId(@Param("musicalBandId") UUID musicalBandId);
}
