package com.bandsyncapi.bandsyncapi.api.v1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bandsyncapi.bandsyncapi.api.v1.models.UsersMusicalBandsKey;
import com.bandsyncapi.bandsyncapi.api.v1.models.UsersMusicalBandsModel;

/*
 * This interface is a repository for the users_musical_bands table in the database.
 * Provides methods for performing CRUD operations on the users_musical_bands table.
 */
public interface UsersMusicalBandsRepository extends JpaRepository<UsersMusicalBandsModel, UsersMusicalBandsKey> {
  
}
