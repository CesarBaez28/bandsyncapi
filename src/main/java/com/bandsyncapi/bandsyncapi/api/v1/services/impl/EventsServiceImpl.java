package com.bandsyncapi.bandsyncapi.api.v1.services.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.bandsyncapi.bandsyncapi.api.v1.dto.events.EventsPutDto;
import com.bandsyncapi.bandsyncapi.api.v1.models.EventsModel;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.EventsRepository;
import com.bandsyncapi.bandsyncapi.api.v1.services.EventsService;

import jakarta.persistence.EntityNotFoundException;

/**
 * Implementation of the EventsService interface.
 */
@Service
public class EventsServiceImpl implements EventsService {

  private final EventsRepository eventsRepository;

  /**
   * Constructor for the EventsServiceImpl class.
   * 
   * @param eventsRepository - Repository for the events table in the database
   */
  public EventsServiceImpl(EventsRepository eventsRepository) {
    this.eventsRepository = eventsRepository;
  }

  @Override
  public EventsModel save(EventsModel eventsModel) {
    return eventsRepository.save(eventsModel);
  }

  @Override
  public List<EventsModel> findByMusicalBandId(UUID musicalBandId) {
    return eventsRepository.findByMusicalBandId(musicalBandId);
  }

  @Override
  public void updateEvent(UUID id, EventsPutDto eventsPutDto) {
    int rowUpdated = eventsRepository.updateEvent(id, eventsPutDto);

    if (rowUpdated == 0) {
      throw new EntityNotFoundException("No se pudo encontrar el evento a actualizar.");
    }    
  }

  @Override
  public void deleteEvent(UUID id) {
    eventsRepository.deleteById(id);
  }
  
}
