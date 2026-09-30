package com.bandsyncapi.bandsyncapi.api.v1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bandsyncapi.bandsyncapi.api.v1.models.TypeOfPermissionsModel;

/**
 * This interface is a repository for the types_permissions table in the database.
 */
public interface TypeOfPermissionsRepository extends JpaRepository<TypeOfPermissionsModel, Integer> {

}
