package com.bandsyncapi.bandsyncapi.api.v1.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bandsyncapi.bandsyncapi.api.v1.dto.events.EventsDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.events.EventsPutDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.events.EventsaPostDto;
import com.bandsyncapi.bandsyncapi.api.v1.mappers.EventsMapper;
import com.bandsyncapi.bandsyncapi.api.v1.models.EventsModel;
import com.bandsyncapi.bandsyncapi.api.v1.services.EventsService;
import com.bandsyncapi.bandsyncapi.response.ApiResponse;

import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * This controller handle requests for the events table in the database.
 */
@RestController
@RequestMapping(path = "/api/v1/events")
public class EventsController {

  private final EventsService eventsService;

  private final EventsMapper eventsMapper;

  /**
   * Constructor for the EventsController class.
   * 
   * @param eventsService - Service for the events table in the database
   * @param eventsMapper  - Mapper for the EventsModel
   */
  public EventsController(EventsService eventsService, EventsMapper eventsMapper) {
    this.eventsService = eventsService;
    this.eventsMapper = eventsMapper;
  }

  /**
   * Save a new event.
   * 
   * @param eventsaPostDto - Event info to save @see EventsaPostDto
   * @return - The new event @see EventsDto
   */
  @PostMapping("/save")
  public ResponseEntity<ApiResponse<EventsDto>> save(@Valid @RequestBody EventsaPostDto eventsaPostDto) {
    EventsModel eventsModel = eventsMapper.toModel(eventsaPostDto);
    EventsModel savedEvent = eventsService.save(eventsModel);
    EventsDto response = eventsMapper.toDto(savedEvent);

    return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true, "Evento creado", response, null));
  }

  /**
   * find all events by musical band id.
   * 
   * @param musicalBandId - musical band id
   * @return - List of events @see EventsDto
   */
  @GetMapping("/findByMusicalBandId/{musicalBandId}")
  public ResponseEntity<ApiResponse<List<EventsDto>>> findByMusicalBandId(@PathVariable UUID musicalBandId) {
    List<EventsModel> eventsModelList = eventsService.findByMusicalBandId(musicalBandId);
    List<EventsDto> response = eventsMapper.toDtoList(eventsModelList);

    if (response.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NO_CONTENT)
          .body(new ApiResponse<>(false, "No se encontraron eventos", null, null));
    }

    return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, "Eventos encontrados", response, null));
  }

  /**
   * Update an event.
   * 
   * @param id            - Event id
   * @param eventsPutDto - Event info to update @see EventsPutDto
   * @return - An ApiResponse object with the message "Evento actualizado"
   */
  @PutMapping("/updateEvent/{id}")
  public ResponseEntity<ApiResponse<Void>> updateEvent(@Valid @PathVariable UUID id, @Valid @RequestBody EventsPutDto eventsPutDto) {
    eventsService.updateEvent(id, eventsPutDto);

    return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, "Evento actualizado", null, null));
  }

  /**
   * Delete an event.
   * 
   * @param id - Event id
   * @return - An ApiResponse object with the message "Evento eliminado"
   */
  @DeleteMapping("/delete/{id}")
  public ResponseEntity<ApiResponse<Void>> deleteEvent(@PathVariable UUID id) {
    eventsService.deleteEvent(id);

    return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, "Evento eliminado", null, null));
  }
}
