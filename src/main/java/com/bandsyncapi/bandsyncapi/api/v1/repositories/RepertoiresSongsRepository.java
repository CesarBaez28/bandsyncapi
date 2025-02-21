package com.bandsyncapi.bandsyncapi.api.v1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bandsyncapi.bandsyncapi.api.v1.models.RepertoiresSongsKey;
import com.bandsyncapi.bandsyncapi.api.v1.models.RepertoiresSongsModel;

/*
 * This interface is a repository for the repertoires_songs table in the database.
 * Provides methods for performing CRUD operations on the repertoires_songs table.
 */
public interface RepertoiresSongsRepository extends JpaRepository<RepertoiresSongsModel, RepertoiresSongsKey>{
  
}
