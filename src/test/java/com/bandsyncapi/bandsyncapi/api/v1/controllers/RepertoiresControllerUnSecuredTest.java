package com.bandsyncapi.bandsyncapi.api.v1.controllers;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
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

import com.bandsyncapi.bandsyncapi.api.v1.dto.repertoires.RepertoiresDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.repertoires.RepertoiresPostDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.repertoires.RepertoiresPutDto;
import com.bandsyncapi.bandsyncapi.api.v1.mappers.RepertoiresMapper;
import com.bandsyncapi.bandsyncapi.api.v1.models.ArtistsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalBandsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalGenresModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.RepertoiresModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.SongsModel;
import com.bandsyncapi.bandsyncapi.api.v1.services.RepertoiresService;
import com.bandsyncapi.bandsyncapi.api.v1.services.RepertoiresSongsService;
import com.bandsyncapi.bandsyncapi.config.TestBeansConfig;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(RepertoiresController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(TestBeansConfig.class)
class RepertoiresControllerUnSecuredTest {

  private static final String BASE_URL = "/api/v1/repertoires";

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private RepertoiresService repertoiresService;

  @MockitoBean
  private RepertoiresSongsService repertoiresSongsService;

  @MockitoBean
  private RepertoiresMapper repertoiresMapper;

  @Test
  void testSave_Valid_Request() throws Exception {
    // Given
    var musicalBandId = UUID.randomUUID();
    var repertoireId = UUID.randomUUID();

    var song = SongsModel.builder()
        .id(1)
        .name("Song Name")
        .genre(new MusicalGenresModel(1))
        .musicalBand(new MusicalBandsModel(musicalBandId))
        .sheetMusic("Sheet Music")
        .artist(new ArtistsModel(1))
        .link("https://example.com/song.mp3")
        .status(true)
        .build();

    List<SongsModel> songsModels = List.of(song);

    var postRequest = new RepertoiresPostDto(
        musicalBandId,
        "Repertoire Name",
        "Repertoire Description",
        "https://example.com/image.png",
        true,
        songsModels);

    var repertoireModel = RepertoiresModel.builder()
        .id(repertoireId)
        .musicalBand(new MusicalBandsModel(musicalBandId))
        .description("Test description")
        .name("repertoire test")
        .status(true)
        .build();

    given(repertoiresMapper.toModel(postRequest)).willReturn(repertoireModel);
    given(repertoiresService.save(repertoireModel)).willReturn(repertoireModel);

    // When
    mockMvc.perform(
        post(BASE_URL + "/save")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(postRequest)))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Repertoire saved successfully"));
  }

  @Test
  void testSave_Invalid_Request() throws Exception {
    // Given
    var musicalBandId = UUID.randomUUID();
    var postRequest = new RepertoiresPostDto(
        musicalBandId,
        "",
        "Repertoire Description",
        "https://example.com/image.png",
        true,
        null);

    // When
    mockMvc.perform(
        post(BASE_URL + "/save")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(postRequest)))
        // Then
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.errors.name").value("El nombre debe tener entre 3 y 100 caracteres."))
        .andExpect(MockMvcResultMatchers.jsonPath("$.errors.songs").value("Debe agregar canciones al repertorio"));
  }

  @Test
  void testFindByMusicalBandId_Valid_Request() throws Exception {
    // Given
    var musicalBandId = UUID.randomUUID();
    var repertoireId = UUID.randomUUID();

    var repertoireModel = RepertoiresModel.builder()
        .id(repertoireId)
        .musicalBand(new MusicalBandsModel(musicalBandId))
        .description("Test description")
        .name("repertoire test")
        .status(true)
        .build();

    var repertoiresDto = new RepertoiresDto(
        repertoireId,
        repertoireModel.getName(),
        repertoireModel.getDescription(),
        repertoireModel.getLink(),
        repertoireModel.getStatus());

    List<RepertoiresModel> repertoiresModelList = List.of(repertoireModel);
    List<RepertoiresDto> repertoiresDtoList = List.of(repertoiresDto);

    given(repertoiresService.findByMusicalBandId(musicalBandId)).willReturn(repertoiresModelList);
    given(repertoiresMapper.toDtoList(repertoiresModelList)).willReturn(repertoiresDtoList);

    // When
    mockMvc.perform(
        get(BASE_URL + "/findByMusicalBandId/" + musicalBandId))
        // Then
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Repertoires found successfully"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").value(repertoireId.toString()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name").value(repertoiresDto.name()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].description").value(repertoiresDto.description()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].link").value(repertoiresDto.link()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].status").value(repertoiresDto.status()));
  }

  @Test
  void testFindByMusicalBandId_Repertoires_Not_Found() throws Exception {
    // Given
    given(repertoiresService.findByMusicalBandId(UUID.randomUUID())).willReturn(List.of());
    given(repertoiresMapper.toDtoList(anyList())).willReturn(List.of());

    // When
    mockMvc.perform(
        get(BASE_URL + "/findByMusicalBandId/" + UUID.randomUUID()))
        // Then
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Repertoires not found"));
  }

  @Test
  void testUpdateRepertoire_Valid_Request() throws Exception {
    // Given
    var repertoireId = UUID.randomUUID();
    var putRequest = new RepertoiresPutDto(
        "new name",
        "new Description",
        "http://locahost",
        true);

    // When
    mockMvc.perform(
        put(BASE_URL + "/updateRepertoire/" + repertoireId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(putRequest)))
        // Then
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
        .andExpect(MockMvcResultMatchers.jsonPath("message").value("Repertoire updated successfully"));
  }

  @Test
  void testUpdateRepertoire_Invalid_Request() throws Exception {
    // Given
    var repertoireId = UUID.randomUUID();

    var putRequest = new RepertoiresPutDto(
        "nc",
        "Test description",
        "http://locahost",
        true);

    // When
    mockMvc.perform(
        put(BASE_URL + "/updateRepertoire/" + repertoireId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(putRequest)))
        // Then
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
        .andExpect(MockMvcResultMatchers.jsonPath("$.errors.name").value("El nombre de tener de 3 a 100 caracteres"));
  }
}
