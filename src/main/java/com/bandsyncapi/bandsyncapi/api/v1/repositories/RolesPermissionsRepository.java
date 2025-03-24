package com.bandsyncapi.bandsyncapi.api.v1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bandsyncapi.bandsyncapi.api.v1.models.RolesModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.RolesPermissionsKey;
import com.bandsyncapi.bandsyncapi.api.v1.models.RolesPermissionsModel;
import java.util.List;

/**
 * This interface is a repository for the roles_permissions table in the database.
 */
@Repository
public interface RolesPermissionsRepository extends JpaRepository<RolesPermissionsModel, RolesPermissionsKey>{

  /**
   * This method finds all the roles_permissions by role.
   * 
   * @param role - the role to find the roles_permissions by.
   * @return - A list of RolesPermissionsModel.
   */
  List<RolesPermissionsModel> findAllByRole(RolesModel role);
}
