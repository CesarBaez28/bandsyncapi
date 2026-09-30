package com.bandsyncapi.bandsyncapi.api.v1.repositories;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bandsyncapi.bandsyncapi.api.v1.models.SetListsModel;

/**
 * This interface defines methods for managing set lists in the application.
 */
public interface SetListsRepository extends JpaRepository<SetListsModel, UUID> {

  /**
   * Finds set lists by musical band id and a search term
   * 
   * @param musicalBandId - musical band id
   * @param term          - search term
   * @param pageable      - Pageable object for pagination
   * @return - Page of SetListsModel
   */
  @Query("""
      SELECT sl FROM SetListsModel sl
      JOIN FETCH sl.musicalBand mb
      JOIN FETCH sl.repertoire r
      WHERE mb.id = :musicalBandId AND
      (
        sl.name LIKE %:term% OR
        r.name LIKE %:term%
      )
      """)
  Page<SetListsModel> findAllByMusicalBandId(@Param("musicalBandId") UUID musicalBandId, @Param("term") String term,
      Pageable pageable);
}
