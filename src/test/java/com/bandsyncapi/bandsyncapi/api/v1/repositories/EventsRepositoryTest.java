package com.bandsyncapi.bandsyncapi.api.v1.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.bandsyncapi.bandsyncapi.api.v1.dto.events.EventsPutDto;
import com.bandsyncapi.bandsyncapi.api.v1.models.EventsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalBandsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.RepertoiresModel;

@DataJpaTest
class EventsRepositoryTest {

  @Autowired
  public MusicalBandsRepository musicalBandsRepository;

  @Autowired
  public EventsRepository eventsRepository;

  @Autowired
  public RepertoiresRepository repertoiresRepository;

  @Test
  void testFindByMusicalBandId() {

    // Given
    var musicalBand = MusicalBandsModel.builder()
        .name("Test Band")
        .hyphenatedName("Test-band")
        .logo("test_logo.png")
        .address("Test Address")
        .phone("123456789")
        .email("testEmail@gmail.com")
        .status(true)
        .build();
    var savedMusicalBand = musicalBandsRepository.save(musicalBand);

    var repertoire = RepertoiresModel.builder()
        .name("Test Repertoire")
        .musicalBand(savedMusicalBand)
        .description("Test")
        .link("http//test.com")
        .status(true)
        .build();
    var savedRepertoire = repertoiresRepository.save(repertoire);

    var testEvent = EventsModel.builder()
        .musicalBand(savedMusicalBand)
        .repertoire(savedRepertoire)
        .date(LocalDateTime.now())
        .name("Test Event")
        .description("Test Event Description")
        .place("Test Place")
        .location("Test Location")
        .status(true)
        .build();

    eventsRepository.save(testEvent);

    // When
    List<EventsModel> events = eventsRepository.findByMusicalBandId(savedMusicalBand.getId());

    // Then
    assertNotNull(events);
    assertFalse(events.isEmpty());
  }

  @Test
  void testUpdateEvent() {

    // Given
    var musicalBand = MusicalBandsModel.builder()
        .name("Test Band")
        .hyphenatedName("Test-band")
        .logo("test_logo.png")
        .address("Test Address")
        .phone("123456789")
        .email("testEmail@gmail.com")
        .status(true)
        .build();
    var savedMusicalBand = musicalBandsRepository.save(musicalBand);

    var repertoire = RepertoiresModel.builder()
        .name("Test Repertoire")
        .musicalBand(savedMusicalBand)
        .description("Test")
        .link("http//test.com")
        .status(true)
        .build();
    var savedRepertoire = repertoiresRepository.save(repertoire);

    var testEvent = EventsModel.builder()
        .musicalBand(savedMusicalBand)
        .repertoire(savedRepertoire)
        .date(LocalDateTime.now())
        .name("Test Event")
        .description("Test Event Description")
        .place("Test Place")
        .location("Test Location")
        .status(true)
        .build();

    var savedEvent = eventsRepository.save(testEvent);

    // When
    var eventPutDto = new EventsPutDto(
      savedEvent.getRepertoire().getId(),
      savedEvent.getDate(),
      "Updated name",
      savedEvent.getDescription(),
      savedEvent.getPlace(),
      savedEvent.getLocation(),
      savedEvent.getStatus());

    int updatedRow = eventsRepository.updateEvent(savedEvent.getId(), eventPutDto);

    // Then
    assertEquals(1, updatedRow);
  }
}
