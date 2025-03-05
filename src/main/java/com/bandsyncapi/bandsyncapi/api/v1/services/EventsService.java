package com.bandsyncapi.bandsyncapi.api.v1.services;

import java.util.List;
import java.util.UUID;

import com.bandsyncapi.bandsyncapi.api.v1.dto.events.EventsPutDto;
import com.bandsyncapi.bandsyncapi.api.v1.models.EventsModel;

/**
 * This interface is a service for the events table in the database.
 */
public interface EventsService {
  
  /**
   * save a new event
   * 
   * @param eventsModel - EventsModel
   * @return - The new event
   */
  EventsModel save (EventsModel eventsModel);

  /**
   * finds all events by musical band id
   * 
   * @param musicalBandId - musical band id
   * @return - List of events
   */
  List<EventsModel> findByMusicalBandId(UUID musicalBandId);

  /**
   * update an event
   * 
   * @param id - Event id
   * @param eventsPutDto - Events info to update @see EventsPutDto
   */
  void updateEvent(UUID id, EventsPutDto eventsPutDto);

  /**
   * delete an event
   * 
   * @param id - event id
   */
  void deleteEvent(UUID id);
}