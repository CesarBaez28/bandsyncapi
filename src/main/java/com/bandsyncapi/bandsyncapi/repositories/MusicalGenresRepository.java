package com.bandsyncapi.bandsyncapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bandsyncapi.bandsyncapi.models.MusicalGenresModel;

@Repository
public interface MusicalGenresRepository extends JpaRepository<MusicalGenresModel, Integer> {
  
}
