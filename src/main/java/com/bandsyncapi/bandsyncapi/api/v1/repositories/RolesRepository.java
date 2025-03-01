package com.bandsyncapi.bandsyncapi.api.v1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bandsyncapi.bandsyncapi.api.v1.models.RolesModel;

/*
 * This interface is a repository for the roles table in the database.
 * Provides methods for performing CRUD operations on the roles table.
 */
public interface RolesRepository extends JpaRepository<RolesModel, Integer>{
  
}
