package com.bandsyncapi.bandsyncapi.api.v1.controllers;

import static org.mockito.BDDMockito.given;
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

import com.bandsyncapi.bandsyncapi.api.v1.dto.songs.SongsDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.songs.SongsPostDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.songs.SongsPutDto;
import com.bandsyncapi.bandsyncapi.api.v1.mappers.SongsMapper;
import com.bandsyncapi.bandsyncapi.api.v1.models.ArtistsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalBandsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalGenresModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.SongsModel;
import com.bandsyncapi.bandsyncapi.api.v1.services.SongsService;
import com.bandsyncapi.bandsyncapi.config.TestBeansConfig;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(SongsController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(TestBeansConfig.class)
class SongsControllerUnSecuredTest {

  private static final String BASE_URL = "/api/v1/songs";

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private SongsService songsService;

  @MockitoBean
  private SongsMapper songsMapper;

  @Test
  void testSave_Valid_Request() throws Exception {
    // Given
    var musicalBandId = UUID.randomUUID();
    var postRequest = new SongsPostDto(
        "Test song",
        new MusicalBandsModel(musicalBandId),
        new ArtistsModel(1),
        new MusicalGenresModel(1),
        "G",
        "http://localhost",
        "http://locahost",
        true);

    var song = SongsModel.builder()
        .id(1)
        .name("Song Name")
        .artist(new ArtistsModel(1))
        .genre(new MusicalGenresModel(1))
        .musicalBand(new MusicalBandsModel(UUID.randomUUID()))
        .tonality("C")
        .link("https://example.com")
        .sheetMusic("Sheet Music")
        .build();

    var songDto = new SongsDto(
        song.getId(),
        song.getName(),
        song.getArtist(),
        song.getGenre(),
        song.getTonality(),
        song.getLink(),
        song.getSheetMusic());

    given(songsMapper.toModel(postRequest)).willReturn(song);
    given(songsService.save(song)).willReturn(song);
    given(songsMapper.toDto(song)).willReturn(songDto);

    // When
    mockMvc.perform(
        post(BASE_URL + "/save")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(postRequest)))
        // Then
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value(songDto.name()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.tonality").value(songDto.tonality()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.link").value(songDto.link()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.sheetMusic").value(songDto.sheetMusic()));
  }

  @Test
  void testSave_Invalid_Request() throws Exception {
    // Given
    var postRequest = new SongsPostDto(
        "Te",
        null,
        null,
        null,
        "G",
        "http://localhost",
        "http://locahost",
        true);

    // When
    mockMvc.perform(
        post(BASE_URL + "/save")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(postRequest)))
        // Then
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
        .andExpect(MockMvcResultMatchers.jsonPath("$.errors.name").value("El nombre debe tener al menos 3 caracteres."))
        .andExpect(MockMvcResultMatchers.jsonPath("$.errors.artist").value("Seleccione un artista."))
        .andExpect(MockMvcResultMatchers.jsonPath("$.errors.genre").value("Seleccione un género."));
  }

  @Test
  void testFindByMusicalBandId_Songs_Found() throws Exception {
    // Given
    var musicalBandId = UUID.randomUUID();

    var song = SongsModel.builder()
        .id(1)
        .name("Song Name")
        .artist(new ArtistsModel(1))
        .genre(new MusicalGenresModel(1))
        .musicalBand(new MusicalBandsModel(musicalBandId))
        .tonality("C")
        .link("https://example.com")
        .sheetMusic("Sheet Music")
        .build();

    var songDto = new SongsDto(
        song.getId(),
        song.getName(),
        song.getArtist(),
        song.getGenre(),
        song.getTonality(),
        song.getLink(),
        song.getSheetMusic());

    List<SongsModel> songsModelList = List.of(song);
    List<SongsDto> songsDtoList = List.of(songDto);

    given(songsService.findByMusicalBandId(musicalBandId)).willReturn(songsModelList);
    given(songsMapper.toDtoList(songsModelList)).willReturn(songsDtoList);

    // When
    mockMvc.perform(
        get(BASE_URL + "/findByMusicalBandId/" + musicalBandId))
        // Then
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Songs found"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name").value(songDto.name()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].tonality").value(songDto.tonality()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].link").value(songDto.link()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].sheetMusic").value(songDto.sheetMusic()));
  }

  @Test
  void testUpdateSong_Valid_Request() throws Exception {
    // Given
    var id = 1;
    var putRequest = new SongsPutDto(
        "New name",
        new ArtistsModel(id),
        new MusicalGenresModel(id),
        "C",
        "http://locahost",
        "http://localhost");

    // When
    mockMvc.perform(
        put(BASE_URL + "/updateSong/" + id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(putRequest)))
        // Then
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Song updated successfully"));
  }

  @Test
  void testUpdateSong_InValid_Request() throws Exception {
    // Given
    var id = 1;
    var putRequest = new SongsPutDto(
        "N",
        null,
        null,
        "C",
        "http://locahost",
        "http://localhost");

    // When
    mockMvc.perform(
        put(BASE_URL + "/updateSong/" + id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(putRequest)))
        // Then
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.errors.name").value("El nombre debe tener entre 3 y 100 caracteres"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.errors.artist").value("Seleccione un artista"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.errors.genre").value("Seleccione un género"));
  }
}
