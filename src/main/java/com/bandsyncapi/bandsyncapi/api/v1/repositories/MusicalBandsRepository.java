package com.bandsyncapi.bandsyncapi.api.v1.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalBandsModel;

/**
 * This interface is repository for the musical_bands table in the database.
 * Provides methods for performing CRUD operations on the musical_bands table.
 */
@Repository
public interface MusicalBandsRepository extends JpaRepository<MusicalBandsModel, UUID> {
  
}
