package com.bandsyncapi.bandsyncapi.api.v1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bandsyncapi.bandsyncapi.api.v1.models.PermissionsModel;

/**
 * This interface is a repository for the permissions table in the database.
 */
@Repository
public interface PermissionsRepository extends JpaRepository<PermissionsModel, Integer>{
  
}
