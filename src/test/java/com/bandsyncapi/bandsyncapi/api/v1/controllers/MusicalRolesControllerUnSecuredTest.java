package com.bandsyncapi.bandsyncapi.api.v1.controllers;

import java.util.List;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

import com.bandsyncapi.bandsyncapi.api.v1.dto.musicalroles.MusicalRolesDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.musicalroles.MusicalRolesPostDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.musicalroles.MusicalRolesPutDto;
import com.bandsyncapi.bandsyncapi.api.v1.mappers.MusicalRolesMapper;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalBandsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalRolesModel;
import com.bandsyncapi.bandsyncapi.api.v1.services.MusicalRolesService;
import com.bandsyncapi.bandsyncapi.config.TestBeansConfig;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.EntityNotFoundException;

@WebMvcTest(MusicalRolesController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(TestBeansConfig.class)
class MusicalRolesControllerUnSecuredTest {

  private static final String BASE_URL = "/api/v1/musical-roles";

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private MusicalRolesService musicalRolesService;

  @MockitoBean
  private MusicalRolesMapper musicalRolesMapper;

  @Test
  void testSave_Valid_Request() throws Exception {
    // Given
    var musicalBandId = UUID.randomUUID();
    var postRequest = new MusicalRolesPostDto(
        "Test role name",
        musicalBandId);

    var musicalRolesModel = MusicalRolesModel.builder()
        .id(1)
        .name(postRequest.name())
        .musicalBand(new MusicalBandsModel(musicalBandId))
        .status(true)
        .build();

    var response = new MusicalRolesDto(
        musicalRolesModel.getId(),
        musicalRolesModel.getName(),
        musicalRolesModel.getStatus());

    given(musicalRolesMapper.toModel(postRequest)).willReturn(musicalRolesModel);
    given(musicalRolesService.save(musicalRolesModel)).willReturn(musicalRolesModel);
    given(musicalRolesMapper.toDto(musicalRolesModel)).willReturn(response);

    // When
    mockMvc.perform(
        post(BASE_URL + "/save")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(postRequest)))
        // Then
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("New role saved successfully"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(response.id()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value(response.name()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.status").value(response.status()));
  }

  @Test
  void testSave_Invalid_Request() throws Exception {
    // Given
    var postRequest = new MusicalRolesPostDto(
        "Te",
        null);

    // When
    mockMvc.perform(
        post(BASE_URL + "/save")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(postRequest)))
        // Then
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.errors.name").value("El nombre debe tener entre 3 y 100 caracteres."));
  }

  @Test
  void testSave_Empty_Request() throws Exception {
    // Given
    var postRequest = new MusicalRolesPostDto("", null);

    // When
    mockMvc.perform(
        post(BASE_URL + "/save")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(postRequest)))
        // Then
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.errors.name").value("El nombre debe tener entre 3 y 100 caracteres."));
  }

  @Test
  void testFindByMusicalBandId_Valid_Request() throws Exception {
    // Given
    var musicalBandId = UUID.randomUUID();

    var musicalRolesModel = MusicalRolesModel.builder()
        .id(1)
        .name("Test role name")
        .musicalBand(new MusicalBandsModel(musicalBandId))
        .status(true)
        .build();

    List<MusicalRolesModel> musicalRolesModels = List.of(musicalRolesModel);

    List<MusicalRolesDto> response = List.of(
        new MusicalRolesDto(
            musicalRolesModel.getId(),
            musicalRolesModel.getName(),
            musicalRolesModel.getStatus()));

    given(musicalRolesService.findByMusicalBandId(musicalBandId)).willReturn(musicalRolesModels);
    given(musicalRolesMapper.toDtoList(musicalRolesModels)).willReturn(response);

    // When
    mockMvc.perform(
        get(BASE_URL + "/findByMusicalBandId/" + musicalBandId))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Musical roles found successfully"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").value(response.get(0).id()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name").value(response.get(0).name()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].status").value(response.get(0).status()));
  }

  @Test
  void testFindByMusicalBandId_Empty_Request() throws Exception {
    // Given
    var musicalBandId = UUID.randomUUID();

    // When
    mockMvc.perform(
        get(BASE_URL + "/findByMusicalBandId/" + musicalBandId))
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("No musical roles found"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());
  }

  @Test
  void testUpdateMusicalRoleName_Valid_Request() throws Exception {
    // Given
    var id = 1;
    var putRequest = new MusicalRolesPutDto("New name");

    doNothing().when(musicalRolesService).updateMusicalRoleName(id, putRequest.name());

    // When
    mockMvc.perform(
        put(BASE_URL + "/updateMusicalRoleName/" + id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(putRequest)))
        // Then
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Musical role name updated successfully"));
  }

  @Test
  void testUpdateMusicalRoleName_Invalid_Request() throws Exception {
    // Given
    var id = 1;
    var putRequest = new MusicalRolesPutDto("Te");

    // When
    mockMvc.perform(
        put(BASE_URL + "/updateMusicalRoleName/" + id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(putRequest)))
        // Then
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.errors.name").value("El nombre debe tener entre 3 y 100 caracteres."));
  }

  @Test
  void testUpdateMusicalRoleName_Empty_Request() throws Exception {
    // Given
    var id = 1;
    var putRequest = new MusicalRolesPutDto("");

    // When
    mockMvc.perform(
        put(BASE_URL + "/updateMusicalRoleName/" + id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(putRequest)))
        // Then
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.errors.name").value("El nombre debe tener entre 3 y 100 caracteres."));
  }

  @Test
  void testUpdateMusicalRoleName_Musical_Role_Not_Exists() throws Exception {
    // Given
    var id = 1;
    var putRequest = new MusicalRolesPutDto("New name");

    doThrow(new EntityNotFoundException("Musical Role not found with id: " + id))
        .when(musicalRolesService).updateMusicalRoleName(id, putRequest.name());

    // When
    mockMvc.perform(
      put(BASE_URL + "/updateMusicalRoleName/" + id)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(putRequest)))
      // Then
      .andExpect(MockMvcResultMatchers.status().isNotFound())
      .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
      .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Musical Role not found with id: " + id));
  }

  @Test
  void testDeleteMusicalRole_Valid_Request() throws Exception {
    // Given
    var id = 1;

    doNothing().when(musicalRolesService).deleteById(id);

    // When
    mockMvc.perform(
        delete(BASE_URL + "/delete/" + id))
        // Then
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Musical role deleted successfully"));
  }
}
