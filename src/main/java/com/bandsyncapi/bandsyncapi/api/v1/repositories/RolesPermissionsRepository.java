package com.bandsyncapi.bandsyncapi.api.v1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bandsyncapi.bandsyncapi.api.v1.models.RolesPermissionsKey;
import com.bandsyncapi.bandsyncapi.api.v1.models.RolesPermissionsModel;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * This interface is a repository for the roles_permissions table in the
 * database.
 */
@Repository
public interface RolesPermissionsRepository extends JpaRepository<RolesPermissionsModel, RolesPermissionsKey> {

  /**
   * This method finds all the roles_permissions by role.
   * 
   * @param role - the role to find the roles_permissions by.
   * @return - A list of RolesPermissionsModel.
   */
  @Query("""
      SELECT rp
      FROM RolesPermissionsModel rp
      JOIN FETCH rp.role r
      JOIN FETCH rp.permission p
      JOIN FETCH p.typeOfPermission
      WHERE r.id = :roleId
      """)
  List<RolesPermissionsModel> findAllByRole(@Param("roleId") Integer roleId);

  /**
   * This method finds all the roles_permissions by roles.
   * 
   * @param roles - list of roles
   * @return - A list of RolesPermissionsModel.
   */
  @Query("""
      SELECT rp
      FROM RolesPermissionsModel rp
      JOIN FETCH rp.role r
      JOIN FETCH rp.permission p
      JOIN FETCH p.typeOfPermission
      WHERE r.id IN :roleIds
      """)
  List<RolesPermissionsModel> findAllByRoleIn(List<Integer> roleIds);

  /**
   * This method finds all the roles_permissions by musical band id.
   * 
   * @param musicalBandId - the musical band id to find the roles_permissions by.
   * @return - A list of RolesPermissionsModel.
   */
  @Query("""
      SELECT rp
      FROM RolesPermissionsModel rp
      JOIN FETCH rp.role r
      JOIN FETCH rp.permission p
      JOIN FETCH p.typeOfPermission
      JOIN FETCH r.musicalBand mb
      WHERE mb.id = :musicalBandId
      """)
  List<RolesPermissionsModel> findByMusicalBandId(@Param("musicalBandId") UUID musicalBandId);

  /**
   * This method deletes all the roles_permissions by role id and permission ids.
   * 
   * @param id                  - the role id to delete the roles_permissions by.
   * @param permissionsToDelete - the list of permission ids to delete the
   *                            roles_permissions by.
   */
  @Transactional
  @Modifying
  @Query("DELETE FROM RolesPermissionsModel r WHERE r.role.id = :id AND r.permission.id IN :permissionsToDelete")
  void deleteByRoleIdAndPermissionIdIn(Integer id, Set<Integer> permissionsToDelete);

  @Transactional
  @Modifying
  @Query(value = "DELETE FROM roles_permissions WHERE role_id = :roleId ", nativeQuery = true)
  void deleteByRoleId(@Param("roleId") Integer roleId);

  /**
   * Delete a list of roles
   * 
   * @param rolesToDelete - Set of integer that contains the role ids to delete
   */
  @Transactional
  @Modifying
  @Query("DELETE FROM RolesPermissionsModel r WHERE r.role.id IN :rolesToDelete")
  void deleteByRoleIdIn(Set<Integer> rolesToDelete);
}
