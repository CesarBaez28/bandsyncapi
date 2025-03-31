package com.bandsyncapi.bandsyncapi.api.v1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bandsyncapi.bandsyncapi.api.v1.models.RolesModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.RolesPermissionsKey;
import com.bandsyncapi.bandsyncapi.api.v1.models.RolesPermissionsModel;

import java.util.List;
import java.util.Set;

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

  /**
   * This method deletes all the roles_permissions by role id and permission ids.
   * 
   * @param id - the role id to delete the roles_permissions by.
   * @param permissionsToDelete - the list of permission ids to delete the roles_permissions by.
   */
  @Transactional
  @Modifying
  @Query("DELETE FROM RolesPermissionsModel r WHERE r.role.id = :id AND r.permission.id IN :permissionsToDelete")
  void deleteByRoleIdAndPermissionIdIn(Integer id, Set<Integer> permissionsToDelete);
}
