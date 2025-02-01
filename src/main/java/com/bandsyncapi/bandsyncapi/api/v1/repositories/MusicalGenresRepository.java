package com.bandsyncapi.bandsyncapi.api.v1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalGenresModel;

/*
 * This interface is a repository for the musical_genres table in the database.
 * Provides methods for performing CRUD operations on the musical_genres table.
 */
@Repository
public interface MusicalGenresRepository extends JpaRepository<MusicalGenresModel, Integer> {
  
}
