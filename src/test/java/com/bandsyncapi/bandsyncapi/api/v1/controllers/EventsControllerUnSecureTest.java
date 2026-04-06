package com.bandsyncapi.bandsyncapi.api.v1.controllers;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.bandsyncapi.bandsyncapi.api.v1.dto.events.EventsDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.events.EventsPostDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.events.EventsPutDto;
import com.bandsyncapi.bandsyncapi.api.v1.mappers.EventsMapper;
import com.bandsyncapi.bandsyncapi.api.v1.models.EventsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalBandsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.RepertoiresModel;
import com.bandsyncapi.bandsyncapi.api.v1.services.EventsService;
import com.bandsyncapi.bandsyncapi.config.TestBeansConfig;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(EventsController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(TestBeansConfig.class)
class EventsControllerUnSecureTest {

  private static final String EVENTS_URL = "/api/v1/events";

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private EventsService eventsService;

  @MockitoBean
  private EventsMapper eventsMapper;

  @Test
  void testSaveEvent_Valid_Event() throws Exception {
    // Given
    var musicalBandId = UUID.randomUUID();
    var repertoireId = UUID.randomUUID();

    var postRequest = new EventsPostDto(
        new MusicalBandsModel(musicalBandId),
        new RepertoiresModel(repertoireId),
        "Event Test",
        LocalDateTime.now(),
        "Test description",
        "Test place",
        "Test location",
        true);

    var eventsModel = EventsModel.builder()
        .musicalBand(new MusicalBandsModel(musicalBandId))
        .repertoire(new RepertoiresModel(repertoireId))
        .name(postRequest.name())
        .date(postRequest.date())
        .description(postRequest.description())
        .place(postRequest.place())
        .location(postRequest.location())
        .status(postRequest.status())
        .build();

    given(eventsMapper.toModel(postRequest)).willReturn(eventsModel);

    eventsModel.setId(UUID.randomUUID());
    given(eventsService.save(eventsModel)).willReturn(eventsModel);

    given(eventsMapper.toDto(eventsModel)).willReturn(
        new EventsDto(
            eventsModel.getId(),
            eventsModel.getRepertoire(),
            eventsModel.getDate(),
            eventsModel.getName(),
            eventsModel.getDescription(),
            eventsModel.getPlace(),
            eventsModel.getLocation(),
            eventsModel.getStatus()));

    // When
    mockMvc.perform(
        post(EVENTS_URL + "/save")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(postRequest)))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value(eventsModel.getName()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.description").value(eventsModel.getDescription()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.place").value(eventsModel.getPlace()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.location").value(eventsModel.getLocation()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.status").value(eventsModel.getStatus()));
  }

  @Test
  void testSaveEvent_Invalid_Post_Request () throws Exception {
    // Given
  
    var postRequest = new EventsPostDto(
        new MusicalBandsModel(UUID.randomUUID()),
        null,
        "",
        null,
        "",
        "",
        "",
        true);

    // When
    mockMvc.perform(
        post(EVENTS_URL + "/save")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(postRequest)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
        .andExpect(MockMvcResultMatchers.jsonPath("$.errors.repertoire").value("Seleccione un repertorio."))
        .andExpect(MockMvcResultMatchers.jsonPath("$.errors.name").value("El nombre debe tener entre 3 y 100 caracteres."))
        .andExpect(MockMvcResultMatchers.jsonPath("$.errors.date").value("La fecha no puede estar vacía."))
        .andExpect(MockMvcResultMatchers.jsonPath("$.errors.place").value("El lugar debe tener entre 3 y 100 caracteres."))
        .andExpect(MockMvcResultMatchers.jsonPath("$.errors.location").value("La ubicación no puede estar vacía."));  

  }

  @Test 
  void testFindByMusicalBandId_Valid_MusicalBandId () throws Exception {
    // Given
    var musicalBandId = UUID.randomUUID(); 
    var eventsModel = EventsModel.builder()
        .id(UUID.randomUUID())
        .musicalBand(new MusicalBandsModel(musicalBandId))
        .name("Event Test")
        .date(LocalDateTime.now())
        .description("Test description")
        .place("Test place")
        .location("Test location")
        .status(true)
        .build();

    given(eventsService.findByMusicalBandId(musicalBandId)).willReturn(List.of(eventsModel));    
    given(eventsMapper.toDtoList(List.of(eventsModel))).willReturn(
        List.of(new EventsDto(
            eventsModel.getId(),
            eventsModel.getRepertoire(),
            eventsModel.getDate(),
            eventsModel.getName(),
            eventsModel.getDescription(),
            eventsModel.getPlace(),
            eventsModel.getLocation(),
            eventsModel.getStatus())));

    // When
    mockMvc.perform(
        get(EVENTS_URL + "/findByMusicalBandId/" + musicalBandId))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name").value(eventsModel.getName()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].description").value(eventsModel.getDescription()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].place").value(eventsModel.getPlace()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].location").value(eventsModel.getLocation()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].status").value(eventsModel.getStatus()));  
  }

  @Test
  void testFindByMusicalBanId_Events_Not_Found () throws Exception { 
    // Given
    var musicalBandId = UUID.randomUUID();

    given(eventsService.findByMusicalBandId(musicalBandId)).willReturn(List.of());
    given(eventsMapper.toDtoList(List.of())).willReturn(List.of());

    // When
    mockMvc.perform(get(EVENTS_URL + "/findByMusicalBandId/" + musicalBandId))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Events Not Found."));
  }

  @Test
  void testUpdateEvent_Valid_Event () throws Exception {
    // Given
    var eventId = UUID.randomUUID();
    var repertoireId = UUID.randomUUID();
    var putRequest = new EventsPutDto(
        repertoireId, 
        LocalDateTime.now(), 
        "New event name", 
        "New description", 
        "New place", 
        "New location", 
        true);

    doNothing().when(eventsService).updateEvent(eventId, putRequest);

    // When
    mockMvc.perform(
        put(EVENTS_URL + "/update/" + eventId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(putRequest)))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Event successfully updated."));
  }

  @Test
  void testUpdateEvent_Request_Body_Invalid ()  throws Exception {
    // Given
    var eventId = UUID.randomUUID();
    var putRequest = new EventsPutDto(
        null, 
        null, 
        "2", 
        "3", 
        "", 
        "", 
        null);

    doNothing().when(eventsService).updateEvent(eventId, putRequest);

    // When
    mockMvc.perform(
        put(EVENTS_URL + "/update/" + eventId)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(putRequest)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
        .andExpect(MockMvcResultMatchers.jsonPath("$.errors.repertoireId").value("Seleccione un repertorio."))
        .andExpect(MockMvcResultMatchers.jsonPath("$.errors.date").value("La fecha no puede estar vacía."))
        .andExpect(MockMvcResultMatchers.jsonPath("$.errors.name").value("El nombre debe tener entre 3 y 100 caracteres."))
        .andExpect(MockMvcResultMatchers.jsonPath("$.errors.place").value("El nombre debe tener entre 3 y 100 caracteres."))
        .andExpect(MockMvcResultMatchers.jsonPath("$.errors.location").value("La ubicación no puede estar vacía."));
  } 
  
  @Test
  void testDeleteEvent_Valid_Event () throws Exception {
    // Given
    var eventId = UUID.randomUUID();

    // When
    mockMvc.perform(delete(EVENTS_URL + "/delete/" + eventId))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Event deleted successfully."));   
  }
}
