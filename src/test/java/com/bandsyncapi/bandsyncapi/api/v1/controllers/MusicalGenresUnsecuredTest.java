package com.bandsyncapi.bandsyncapi.api.v1.controllers;

import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.bandsyncapi.bandsyncapi.api.v1.dto.musicalgenres.MusicalGenreDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.musicalgenres.MusicalGenrePostDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.musicalgenres.MusicalGenrePutDto;
import com.bandsyncapi.bandsyncapi.api.v1.mappers.MusicalGenresMapper;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalBandsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalGenresModel;
import com.bandsyncapi.bandsyncapi.api.v1.services.MusicalGenresService;
import com.bandsyncapi.bandsyncapi.config.TestBeansConfig;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.EntityNotFoundException;

@WebMvcTest(MusicalGenresController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(TestBeansConfig.class)
class MusicalGenresUnsecuredTest {

  private static final String BASE_URL = "/api/v1/musical-genres";

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private MusicalGenresService musicalGenresService;

  @MockitoBean
  private MusicalGenresMapper musicalGenresMapper;

  @Test
  void testSaveMusicalGenre_ValidRequest() throws Exception {
    // Given
    var musicalBandId = UUID.randomUUID();
    var postRequest = new MusicalGenrePostDto(
        "Test genre",
        musicalBandId);

    var musicalGenreModel = MusicalGenresModel.builder()
        .id(1)
        .name(postRequest.name())
        .musicalBand(new MusicalBandsModel(musicalBandId))
        .status(true)
        .build();

    var musicalGenreDtp = new MusicalGenreDto(
        musicalGenreModel.getId(),
        musicalGenreModel.getName(),
        musicalGenreModel.getStatus());

    given(musicalGenresMapper.toModel(postRequest)).willReturn(musicalGenreModel);    
    given(musicalGenresService.save(musicalGenreModel)).willReturn(musicalGenreModel);
    given(musicalGenresMapper.toDto(musicalGenreModel)).willReturn(musicalGenreDtp);

    // When
    mockMvc.perform(
      post(BASE_URL + "/save")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(postRequest)))
      // Then
      .andExpect(MockMvcResultMatchers.status().isCreated())
      .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
      .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Musical genre successfully created."))
      .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(musicalGenreDtp.id()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value(musicalGenreDtp.name()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.data.status").value(musicalGenreDtp.status()));
  }

  @Test
  void testSaveMusicalGenre_InvalidRequest() throws Exception {
    // Given
    var postRequest = new MusicalGenrePostDto(
        "te",
        null);

    // When
    mockMvc.perform(
      post(BASE_URL + "/save")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(postRequest)))
      // Then
      .andExpect(MockMvcResultMatchers.status().isBadRequest())
      .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
      .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Las validaciones de los campos fallaron."))
      .andExpect(MockMvcResultMatchers.jsonPath("$.errors.name").value("El nombre debe tener entre 3 y 100 caracteres."));
  } 

  @Test
  void testSaveMusicalGenre_EmptyRequest() throws Exception {
    // Given
    var postRequest = new MusicalGenrePostDto(
        "",
        null);

    // When
    mockMvc.perform(
      post(BASE_URL + "/save")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(postRequest)))
      // Then
      .andExpect(MockMvcResultMatchers.status().isBadRequest())
      .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
      .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Las validaciones de los campos fallaron."))
      .andExpect(MockMvcResultMatchers.jsonPath("$.errors.name").value("El nombre debe tener entre 3 y 100 caracteres."));
  }

  @Test
  void testUpdateMusicalGenreName_ValidRequest () throws Exception {
    // Given
    var id = 1;
    var putRequest = new MusicalGenrePutDto("Test new genre name");

    // When
    mockMvc.perform(
      put(BASE_URL + "/updateMusicalGenreName/" + id)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(putRequest)))
      // Then
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
      .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Musical genre name updated successfully."));
  }

  @Test
  void testUpdateMusicalGenreName_InvalidRequest () throws Exception {
    // Given
    var id = 1;
    var putRequest = new MusicalGenrePutDto("d");

    // When
    mockMvc.perform(
      put(BASE_URL + "/updateMusicalGenreName/" + id)
         .contentType(MediaType.APPLICATION_JSON)
         .content(objectMapper.writeValueAsString(putRequest)))
      // Then
      .andExpect(MockMvcResultMatchers.status().isBadRequest())
      .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
      .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Las validaciones de los campos fallaron."))
      .andExpect(MockMvcResultMatchers.jsonPath("$.errors.name").value("El nombre debe tener entre 3 y 100 caracteres."));
  }

  @Test
  void testUpdateMusicalGenreName_EmptyRequest () throws Exception {
    // Given
    var id = 1;
    var putRequest = new MusicalGenrePutDto("");

    // When
    mockMvc.perform(
      put(BASE_URL + "/updateMusicalGenreName/" + id)
         .contentType(MediaType.APPLICATION_JSON)
         .content(objectMapper.writeValueAsString(putRequest)))
      // Then
      .andExpect(MockMvcResultMatchers.status().isBadRequest())
      .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
      .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Las validaciones de los campos fallaron."))
      .andExpect(MockMvcResultMatchers.jsonPath("$.errors.name").value("El nombre debe tener entre 3 y 100 caracteres."));
  }

  @Test
  void testUpdateMusicalGenreName_NotFound () throws Exception {
    // Given
    var id = 1;
    var putRequest = new MusicalGenrePutDto("Test new genre name");

    doThrow(new EntityNotFoundException("Musical genre not found"))
        .when(musicalGenresService).updateGenreName(id, putRequest.name());

    // When
    mockMvc.perform(
      put(BASE_URL + "/updateMusicalGenreName/" + id)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(putRequest)))
      // Then
      .andExpect(MockMvcResultMatchers.status().isNotFound())
      .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
      .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Musical genre not found"));
  }

  @Test
  void testDeleteMusicalGenre_ValidRequest () throws Exception {
    // Given
    var id = 1;

    // When
    mockMvc.perform(
      delete(BASE_URL + "/delete/" + id))
      // Then
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
      .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Musical genre deleted successfully."));
    }
}