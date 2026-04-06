package com.bandsyncapi.bandsyncapi.api.v1.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bandsyncapi.bandsyncapi.api.v1.dto.events.EventsPutDto;
import com.bandsyncapi.bandsyncapi.api.v1.models.EventsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalBandsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.RepertoiresModel;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.EventsRepository;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class EventsServiceImplTest {

  @Mock
  private EventsRepository eventsRepository;

  private EventsServiceImpl eventsServiceImpl;

  @BeforeEach
  void setUp() {
    eventsServiceImpl = new EventsServiceImpl(eventsRepository);
  }

  @Test
  void testSaveEvent() {
    // Given
    var event = EventsModel.builder()
        .name("Event Test")
        .date(LocalDateTime.now())
        .description("Description test")
        .location("Location test")
        .place("Place Test")
        .musicalBand(new MusicalBandsModel(UUID.randomUUID()))
        .repertoire(new RepertoiresModel(UUID.randomUUID()))
        .status(true)
        .build();

    // When
    eventsServiceImpl.save(event);

    // Then
    ArgumentCaptor<EventsModel> captor = ArgumentCaptor.forClass(EventsModel.class);

    verify(eventsRepository).save(captor.capture());

    EventsModel eventCaptured = captor.getValue();

    assertEquals(eventCaptured, event);
  }

  @Test
  void testFindByMusicalBandId() {
    // Given
    var musicalBandId = UUID.randomUUID();

    // When
    eventsServiceImpl.findByMusicalBandId(musicalBandId);

    // Then
    verify(eventsRepository).findByMusicalBandId(musicalBandId);
  }

  @Test
  void testUpdateEvent() {
    // Given
    var eventId = UUID.randomUUID();

    var eventPutDto = new EventsPutDto(
        UUID.randomUUID(),
        LocalDateTime.now(),
        "New name",
        "New Description",
        "New Place",
        "New location",
        true);

    given(eventsRepository.updateEvent(eventId, eventPutDto)).willReturn(1);

    // When
    eventsServiceImpl.updateEvent(eventId, eventPutDto);

    // Then
    verify(eventsRepository).updateEvent(eventId, eventPutDto);
  }

  @Test
  void testUpdateEventNotFound() {
    // Given
    var eventId = UUID.randomUUID();

    var eventPutDto = new EventsPutDto(
        UUID.randomUUID(),
        LocalDateTime.now(),
        "New name",
        "New Description",
        "New Place",
        "New location",
        true);

    // When
    given(eventsRepository.updateEvent(eventId, eventPutDto)).willReturn(0);

    // Then
    assertThrows(EntityNotFoundException.class, () -> {
      eventsServiceImpl.updateEvent(eventId, eventPutDto);
    });

    verify(eventsRepository).updateEvent(eventId, eventPutDto);
  }
}
