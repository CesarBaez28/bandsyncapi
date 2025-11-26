package com.bandsyncapi.bandsyncapi.api.v1.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bandsyncapi.bandsyncapi.api.v1.models.InvitationsModel;
import java.util.Optional;


/**
 * Repository for the InvitationsModel to manage CRUD operations
 */
@Repository
public interface InvitationsRepository extends JpaRepository<InvitationsModel, UUID> {

  /**
   * find an invitation by token
   * 
   * @param token - token
   * @return - Optional of type InvitationsModel
   */
  Optional<InvitationsModel> findByToken(String token);
}
