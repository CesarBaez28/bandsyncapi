package com.bandsyncapi.bandsyncapi.api.v1.projections;

/**
 * Projection interface for fetching musical roles of a specific user.
 * This interface is used to define the structure of the data
 * that will be returned by the query in the repository.
 */
public interface MusicalRolesSingleUserProjection {
  Integer getId();
  String getName();
  Boolean getStatus();
}
