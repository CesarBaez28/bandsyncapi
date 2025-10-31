package com.bandsyncapi.bandsyncapi.api.v1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.bandsyncapi.bandsyncapi.api.v1.models.RolesModel;

/*
 * This interface is a repository for the roles table in the database.
 * Provides methods for performing CRUD operations on the roles table.
 */
public interface RolesRepository extends JpaRepository<RolesModel, Integer>{


  /**
   * This method updates the name of a role by its id.
   * 
   * @param name - The new name of the role.
   * @param id   - The id of the role to update.
   * @return - The number of rows affected.
   */
  @Transactional
  @Modifying
  @Query("UPDATE RolesModel r SET r.name = :name WHERE r.id = :id")
  int updateRoleNameById(@Param("name") String name, @Param("id") Integer id);

  @Transactional
  @Modifying
  @Query(value = "DELETE FROM roles where id = :roleId", nativeQuery = true)
  void deleteByRoleId(@Param("roleId") Integer roleId);
}
