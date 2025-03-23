package com.bandsyncapi.bandsyncapi.api.v1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bandsyncapi.bandsyncapi.api.v1.models.UsersRolesModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.UsersRolesKey;

/**
 * This interface is a repository for the users_roles table in the database.
 * Provides methods for performing CRUD operations on the users_roles table.
 */
@Repository
public interface UsersRolesRepository extends JpaRepository<UsersRolesModel, UsersRolesKey> {
  
}
