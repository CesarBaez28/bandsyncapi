package com.bandsyncapi.bandsyncapi.api.v1.controllers;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

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

import com.bandsyncapi.bandsyncapi.api.v1.dto.artists.ArtistsDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.artists.ArtistsPostDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.artists.ArtistsPutDto;
import com.bandsyncapi.bandsyncapi.api.v1.mappers.ArtistsMapper;
import com.bandsyncapi.bandsyncapi.api.v1.models.ArtistsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalBandsModel;
import com.bandsyncapi.bandsyncapi.api.v1.services.ArtistsService;
import com.bandsyncapi.bandsyncapi.config.TestBeansConfig;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.EntityNotFoundException;

@WebMvcTest(ArtistsController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(TestBeansConfig.class)
class ArtistsControllerUnSecuredTest {

  private static final String ARTISTS_URL = "/api/v1/artists";

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private ArtistsService artistsService;

  @MockitoBean
  private ArtistsMapper artistsMapper;

  @Test
  void testSaveArtist_Valid_Artist() throws Exception {
    // Given
    var artistsPostDto = new ArtistsPostDto("Artist name", UUID.randomUUID());

    var artistsModel = ArtistsModel.builder()
        .name(artistsPostDto.name())
        .musicalBand(new MusicalBandsModel(artistsPostDto.musicalBandId()))
        .status(true)
        .build();

    given(artistsMapper.toModel(artistsPostDto)).willReturn(artistsModel);

    given(artistsService.save(artistsModel)).willReturn(artistsModel);

    given(artistsMapper.toDto(artistsModel))
        .willReturn(new ArtistsDto(artistsModel.getId(), artistsModel.getName(), artistsModel.getStatus()));

    // When
    mockMvc.perform(post(ARTISTS_URL + "/save")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(artistsPostDto)))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value(artistsModel.getName()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true));
  }

  @Test
  void testSaveArtist_Empty_Artist_Name() throws Exception {
    // Given
    var artistsPostDto = new ArtistsPostDto("", UUID.randomUUID());

    // When
    mockMvc.perform(post(ARTISTS_URL + "/save")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(artistsPostDto)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
        .andExpect(MockMvcResultMatchers.jsonPath("$.errors.name").value("El campo nombre es obligatorio."));
  }

  @Test
  void testSaveArtist_Artist_Name_Too_Short() throws Exception {
    // Given
    var artistsPostDto = new ArtistsPostDto("A", UUID.randomUUID());

    // When
    mockMvc.perform(post(ARTISTS_URL + "/save")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(artistsPostDto)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
        .andExpect(MockMvcResultMatchers.jsonPath("$.errors.name").value("El nombre debe tener entre 3 y 100 caracteres."));
  }

  @Test
  void testFindByMusicalBandId_Valid_MusicalBandId() throws Exception {
    // Given
    var musicalBandId = UUID.randomUUID();

    var artistsModel = ArtistsModel.builder()
        .name("Artist name")
        .musicalBand(new MusicalBandsModel(musicalBandId))
        .status(true)
        .build();

    given(artistsService.findByMusicalBandId(musicalBandId)).willReturn(List.of(artistsModel));

    given(artistsMapper.toDtoList(List.of(artistsModel)))
        .willReturn(List.of(new ArtistsDto(artistsModel.getId(), artistsModel.getName(), artistsModel.getStatus())));

    // When
    mockMvc.perform(get(ARTISTS_URL + "/findByMusicalBandId/" + musicalBandId))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name").value(artistsModel.getName()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true));
  }  

  @Test
  void testFindByMusicalBandId_Invalid_MusicalBandId() throws Exception {
    // Given
    var musicalBandId = UUID.randomUUID();

    given(artistsService.findByMusicalBandId(musicalBandId)).willReturn(List.of());

    // When
    mockMvc.perform(get(ARTISTS_URL + "/findByMusicalBandId/" + musicalBandId))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true));
  }

  @Test
  void testUpdateArtistName_Valid_Artist() throws Exception {
    // Given
    var artistId = 1;
    var putRequest = new ArtistsPutDto("Updated Artist Name");

    // When
    mockMvc.perform(put(ARTISTS_URL + "/updateArtistName/" + artistId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(putRequest)))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true));
  }
   
  @Test
  void testUpdateArtistName_Empty_ArtistName() throws Exception {
    // Given
    var artistId = 1;
    var putRequest = new ArtistsPutDto("");

    // When
    mockMvc.perform(put(ARTISTS_URL + "/updateArtistName/" + artistId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(putRequest)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
        .andExpect(MockMvcResultMatchers.jsonPath("$.errors.name").value("El campo nombre es obligatorio."));
  }

  @Test
  void testUpdateArtistName_Artist_Name_Too_Short() throws Exception {
    // Given
    var artistId = 1;
    var putRequest = new ArtistsPutDto("A");

    // When
    mockMvc.perform(put(ARTISTS_URL + "/updateArtistName/" + artistId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(putRequest)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
        .andExpect(MockMvcResultMatchers.jsonPath("$.errors.name").value("El nombre debe tener entre 3 y 100 caracteres."));
  }

  @Test
  void testUpdateArtistName_Artist_Not_Found() throws Exception {
    // Given 
    var artistId = 1;
    var putRequest = new ArtistsPutDto("Updated Artist Name");

    doThrow(new EntityNotFoundException("Artist not found"))
        .when(artistsService).updateArtist(artistId, putRequest.name());

    // When
    mockMvc.perform(put(ARTISTS_URL + "/updateArtistName/" + artistId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(putRequest)))
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Artist not found"));
  }

  @Test
  void testDeleteArtist_Valid_Artist() throws Exception {
    // Given
    var artistId = 1;

    // When
    mockMvc.perform(delete(ARTISTS_URL + "/delete/" + artistId))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true));
  }
}
