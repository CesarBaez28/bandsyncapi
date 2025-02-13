package com.bandsyncapi.bandsyncapi.api.v1.services;

import java.util.List;
import java.util.UUID;

import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalRolesModel;

/*
 * This interface is a service for the musical_roles table in the database.
 * Defines methods for performing CRUD operations on the musical_roles table.
 */
public interface MusicalRolesService {

  /**
   * save a new musical Role
   * @param musicalRolesModel - MusicalRoleModel
   * @return - The new Musical Role
   */
  public MusicalRolesModel save (MusicalRolesModel musicalRolesModel);

  /**
   * finds musical roles by musical band id
   * @param id
   * @return
   */
  public List<MusicalRolesModel> findByMusicalBandId (UUID id);

  /**
   * Update musical genre name
   * @param id - musical role id
   * @param name - musical role name
   */
  public void updateMusicalRoleName(Integer id, String name);

  /**
   * Deletes a musical roles from the database by its id.
   * @param id - Id of the musical roles to be deleted.
   */
  public void deleteById(Integer id);
}
