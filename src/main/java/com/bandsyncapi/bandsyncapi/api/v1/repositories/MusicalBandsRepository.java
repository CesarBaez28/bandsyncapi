package com.bandsyncapi.bandsyncapi.api.v1.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalBandsModel;
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
   * @param logo - new url logo
   * @return number of rows updated
   */
  @Transactional
  @Modifying
  @Query("UPDATE MusicalBandsModel mb SET mb.logo = :logo WHERE mb.id = :musicalBandId")
  int updateLogoById (@Param("musicalBandId") UUID musicalBandId, @Param("logo") String logo);
}
