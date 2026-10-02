package com.bandsyncapi.bandsyncapi.api.v1.projections;

import java.util.UUID;

/**
 * Projection interface for fetching musical roles of users.
 * This interface is used to define the structure of the data
 * that will be returned by the query in the repository.
 */
public interface MusicalRolesUsersProjection {
  UUID getUserId();
  Integer getId();
  String getName();
  Boolean getStatus();
}
