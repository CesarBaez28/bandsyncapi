package com.bandsyncapi.bandsyncapi.api.v1.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bandsyncapi.bandsyncapi.api.v1.dto.musicalbands.MusicalBandPutDto;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalBandsModel;

import java.util.List;
import java.util.Optional;

/**
 * This interface is repository for the musical_bands table in the database.
 * Provides methods for performing CRUD operations on the musical_bands table.
 */
@Repository
public interface MusicalBandsRepository extends JpaRepository<MusicalBandsModel, UUID> {

  /**
   * Check if the musical band exists by id
   * 
   * @param id - Musical band id
   * @return - true if exists, false otherwise
   */
  boolean existsById(@NonNull UUID id);

  /**
   * find musical band by hyphenated name
   * 
   * @param name - musical band name
   * @return A Optional Object with the MusicalBandsModel if it is found
   */
  Optional<MusicalBandsModel> findByHyphenatedName(String name);

  /**
   * Update logo by id
   * 
   * @param musicalBandId - musical band id
   * @param logo          - new url logo
   * @return number of rows updated
   */
  @Transactional
  @Modifying
  @Query("UPDATE MusicalBandsModel mb SET mb.logo = :logo WHERE mb.id = :musicalBandId")
  int updateLogoById(@Param("musicalBandId") UUID musicalBandId, @Param("logo") String logo);

  /**
   * Update user info
   * 
   * @param musicalBandId - musical band id
   * @param newData       - new info to be uptaded
   * @return - number of rows updated
   */
  @Transactional
  @Modifying
  @Query("""
      UPDATE MusicalBandsModel mb
      SET mb.name = :#{#newData.name},
          mb.address = :#{#newData.address},
          mb.phone = :#{#newData.phone},
          mb.email = :#{#newData.email},
          mb.logo = :logo
      WHERE mb.id = :musicalBandId
      """)
  int update(@Param("musicalBandId") UUID musicalBandId, @Param("newData") MusicalBandPutDto newData,
      @Param("logo") String logo);

  /**
   * Delete a musical band by id
   * 
   * @param id - musical band id
   */
  @Transactional
  @Modifying
  @Query(value = "DELETE FROM musical_bands WHERE id = :id", nativeQuery = true)
  void deleteByBandId(@Param("id") UUID id);
}
