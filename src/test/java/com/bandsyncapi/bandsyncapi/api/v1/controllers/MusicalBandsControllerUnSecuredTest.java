package com.bandsyncapi.bandsyncapi.api.v1.controllers;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.bandsyncapi.bandsyncapi.api.v1.dto.musicalbands.MusicalBandsDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.musicalbands.MusicalBandsPostDto;
import com.bandsyncapi.bandsyncapi.api.v1.models.UsersModel;
import com.bandsyncapi.bandsyncapi.api.v1.services.MusicalBandsService;
import com.bandsyncapi.bandsyncapi.api.v1.services.UsersMusicalBandsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.bandsyncapi.bandsyncapi.config.TestBeansConfig;

@WebMvcTest(MusicalBandsController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(TestBeansConfig.class)
class MusicalBandsControllerUnSecuredTest {

  private static final String BASE_URL = "/api/v1/musical-bands";

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private MusicalBandsService musicalBandsService;

  @MockitoBean
  private UsersMusicalBandsService usersMusicalBandsService;

  @Test
  void testSaveMusicalBand_Valid_Post_Request () throws Exception {
    // Given
    var musicalBandId = UUID.randomUUID();
    var userId = UUID.randomUUID();
    var postRequestObject = new MusicalBandsPostDto(
      musicalBandId, 
      "Test Band", 
      "123 Test St", 
      "1234567890", 
      "test@gmail.com", 
      true, 
      new UsersModel(userId));

    MockMultipartFile request = new MockMultipartFile(
      "musicalBand", 
      "", 
      "application/json", 
      objectMapper.writeValueAsString(postRequestObject).getBytes()
    );

    var savedMusicalBand = new MusicalBandsDto(
       postRequestObject.id(), 
       "Test-Band",
       postRequestObject.name(), 
       "https://logo.com", 
       postRequestObject.address(), 
       postRequestObject.phone(), 
       postRequestObject.email(), 
       postRequestObject.status());

    given(musicalBandsService.registerMusicalBand(postRequestObject, null))
      .willReturn(savedMusicalBand);

    // When
    mockMvc.perform(
      multipart(BASE_URL + "/save")
        .file(request)
        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
      .andExpect(MockMvcResultMatchers.status().isCreated())
      .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
      .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Banda musical registrada correctamente")) 
      .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(savedMusicalBand.id().toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value(savedMusicalBand.name()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.data.logo").value(savedMusicalBand.logo()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.data.address").value(savedMusicalBand.address()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.data.phone").value(savedMusicalBand.phone()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.data.email").value(savedMusicalBand.email()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.data.status").value(savedMusicalBand.status()));
  }

  @Test
  void testSaveMusicalBand_Bad_Request () throws Exception {
    // Given
    var musicalBandId = UUID.randomUUID();
    var userId = UUID.randomUUID();
    var postRequestObject = new MusicalBandsPostDto(
      musicalBandId, 
      "", 
      "123 Test St", 
      "1234567890", 
      "", 
      true, 
      new UsersModel(userId));

    MockMultipartFile request = new MockMultipartFile(
      "musicalBand", 
      "", 
      "application/json", 
      objectMapper.writeValueAsString(postRequestObject).getBytes()
    );

    // When
    mockMvc.perform(multipart(BASE_URL + "/save")
        .file(request)
        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
      .andExpect(MockMvcResultMatchers.status().isBadRequest())
      .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
      .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Las validaciones de los campos fallaron."))
      .andExpect(MockMvcResultMatchers.jsonPath("$.errors.name").value("El nombre debe tener entre 3 y 100 caracteres."))
      .andExpect(MockMvcResultMatchers.jsonPath("$.errors.email").value("El email es obligatorio."));
  }

  @Test
  void testSaveMusicalBand_Bad_Request_Invalid_Email () throws Exception {
    // Given
    var musicalBandId = UUID.randomUUID();
    var userId = UUID.randomUUID();
    var postRequestObject = new MusicalBandsPostDto(
      musicalBandId, 
      "", 
      "123 Test St", 
      "1234567890", 
      "cesar.test.com", 
      true, 
      new UsersModel(userId));

    MockMultipartFile request = new MockMultipartFile(
      "musicalBand", 
      "", 
      "application/json", 
      objectMapper.writeValueAsString(postRequestObject).getBytes()
    );

    // When
    mockMvc.perform(multipart(BASE_URL + "/save")
        .file(request)
        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
      .andExpect(MockMvcResultMatchers.status().isBadRequest())
      .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
      .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Las validaciones de los campos fallaron."))
      .andExpect(MockMvcResultMatchers.jsonPath("$.errors.name").value("El nombre debe tener entre 3 y 100 caracteres."))
      .andExpect(MockMvcResultMatchers.jsonPath("$.errors.email").value("El email debe ser válido."));
  }

  @Test
  void testFindByUserId_Data_Found () throws Exception {
    // Given
    var userId = UUID.randomUUID();

    var musicalBandDto = new MusicalBandsDto(
      UUID.randomUUID(), 
      "Test band", 
      "test-band", 
      "http://localhost", 
      "test address", 
      "8093457653", 
      "test@gmail.com", 
      true);

    List<MusicalBandsDto> list = List.of(musicalBandDto);

    given(usersMusicalBandsService.findByUser(new UsersModel(userId))).willReturn(list);

    // When
    mockMvc.perform(
      get(BASE_URL + "/findByUserId/" + userId))
      // Then
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
      .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name").value(musicalBandDto.name()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].hyphenatedName").value(musicalBandDto.hyphenatedName()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].logo").value(musicalBandDto.logo()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].address").value(musicalBandDto.address()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].phone").value(musicalBandDto.phone()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].email").value(musicalBandDto.email()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].status").value(musicalBandDto.status()));
  }
}
