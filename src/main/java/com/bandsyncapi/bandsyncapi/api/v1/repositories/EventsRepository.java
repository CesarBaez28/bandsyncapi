package com.bandsyncapi.bandsyncapi.api.v1.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bandsyncapi.bandsyncapi.api.v1.dto.events.EventsPutDto;
import com.bandsyncapi.bandsyncapi.api.v1.models.EventsModel;

import jakarta.transaction.Transactional;

/**
 * This interface is a repository for the events table in the database.
 */
public interface EventsRepository extends JpaRepository<EventsModel, UUID> {

  /**
   * This method finds all events by musical band id.
   * 
   * @param musicalBandId - musical band id
   * @return
   */
  @Query("SELECT e FROM EventsModel e WHERE e.musicalBand.id = :musicalBandId")
  List<EventsModel> findByMusicalBandId(UUID musicalBandId);

  /**
   * This method updates an event.
   * 
   * @param id - Event id
   * @param eventsPutDto - Events info to update @see EventsPutDto
   * @return - Returns the number of rows affected
   */
  @Transactional
  @Modifying
  @Query("""
      UPDATE EventsModel e 
      SET e.repertoire.id = :#{#EventsPutDto.repertoireId},
          e.date = :#{#EventsPutDto.date},
          e.name = :#{#EventsPutDto.name},
          e.description = :#{#EventsPutDto.description}, 
          e.place = :#{#EventsPutDto.place}, 
          e.location = :#{#EventsPutDto.location},
          e.status = :#{#EventsPutDto.status} 
      WHERE e.id = :id
      """)
  int updateEvent(@Param("id") UUID id, @Param("EventsPutDto") EventsPutDto eventsPutDto);
}
