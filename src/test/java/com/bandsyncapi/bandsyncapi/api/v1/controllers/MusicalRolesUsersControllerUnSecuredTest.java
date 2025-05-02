package com.bandsyncapi.bandsyncapi.api.v1.controllers;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

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

import com.bandsyncapi.bandsyncapi.api.v1.dto.musicalroles.MusicalRolesDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.users.MusicalRolesUsersDto;
import com.bandsyncapi.bandsyncapi.api.v1.mappers.MusicalRolesMapper;
import com.bandsyncapi.bandsyncapi.api.v1.mappers.MusicalRolesUsersMapper;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalBandsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalRolesModel;
import com.bandsyncapi.bandsyncapi.api.v1.projections.MusicalRolesSingleUserProjection;
import com.bandsyncapi.bandsyncapi.api.v1.projections.MusicalRolesUsersProjection;
import com.bandsyncapi.bandsyncapi.api.v1.services.MusicalRolesUsersService;
import com.bandsyncapi.bandsyncapi.config.TestBeansConfig;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(MusicalRolesUsersController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(TestBeansConfig.class)
class MusicalRolesUsersControllerUnSecuredTest {

  private static final String BASE_URL = "/api/v1/musical-roles-users";

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private MusicalRolesUsersService musicalRolesUsersService;

  @MockitoBean
  private MusicalRolesUsersMapper musicalRolesUsersMapper;

  @MockitoBean
  private MusicalRolesMapper musicalRolesMapper;

  @Test
  void testFindAllByMusicalBandId_Valid_Request() throws Exception {
    // Given
    var musicalBandId = UUID.randomUUID();
    var userId = UUID.randomUUID();

    MusicalRolesUsersProjection musicalRolesUsersProjection = mock(MusicalRolesUsersProjection.class);
    given(musicalRolesUsersProjection.getUserId()).willReturn(userId);
    List<MusicalRolesUsersProjection> list = List.of(musicalRolesUsersProjection);

    var musicalRoleDto = new MusicalRolesDto(1, "Test", true);
    List<MusicalRolesDto> musicalRolesDtoList = List.of(musicalRoleDto);

    var musicalRolesUsersDto = new MusicalRolesUsersDto(musicalRolesUsersProjection.getUserId(), musicalRolesDtoList);
    List<MusicalRolesUsersDto> musicalRolesUsersDtoList = List.of(musicalRolesUsersDto);

    given(musicalRolesUsersService.findAllByMusicalBandId(musicalBandId)).willReturn(list);
    given(musicalRolesUsersMapper.toDtoList(list)).willReturn(musicalRolesUsersDtoList);

    // When
    mockMvc.perform(
        get(BASE_URL + "/findAllByMusicalBandId/" + musicalBandId))
        // Then
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].userId").value(userId.toString()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].musicalRoles[0].id").value(musicalRoleDto.id()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].musicalRoles[0].name").value(musicalRoleDto.name()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].musicalRoles[0].status").value(musicalRoleDto.status()));
  }

  @Test
  void testFindAllByMusicalBandId_EmptyList() throws Exception {
    // Given
    var musicalBandId = UUID.randomUUID();

    List<MusicalRolesUsersProjection> list = List.of();

    given(musicalRolesUsersService.findAllByMusicalBandId(musicalBandId)).willReturn(list);

    // When
    mockMvc.perform(
        get(BASE_URL + "/findAllByMusicalBandId/" + musicalBandId))
        // Then
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Users musical roles not found"));
  }

  @Test
  void testFindMusicalRolesUser_Valid_Request() throws Exception {
    // Given
    var musicalBandId = UUID.randomUUID();
    var userId = UUID.randomUUID();

    MusicalRolesSingleUserProjection projection = new MusicalRolesSingleUserProjection() {
      @Override
      public Integer getId() {
        return 1;
      }

      @Override
      public String getName() {
        return "Test";
      }

      @Override
      public Boolean getStatus() {
        return true;
      }
    };

    List<MusicalRolesSingleUserProjection> response = List.of(projection);

    given(musicalRolesUsersService.findMusicalRolesUser(musicalBandId, userId)).willReturn(response);

    // When
    mockMvc.perform(
        get(BASE_URL + "/findMusicalRolesUser/" + musicalBandId + "/" + userId))
        // Then
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").value(projection.getId()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name").value(projection.getName()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].status").value(projection.getStatus()));
  }

  @Test
  void testFindMusicalRolesUser_EmptyList() throws Exception {
    // Given
    var musicalBandId = UUID.randomUUID();
    var userId = UUID.randomUUID();

    List<MusicalRolesSingleUserProjection> response = List.of();

    given(musicalRolesUsersService.findMusicalRolesUser(musicalBandId, userId)).willReturn(response);

    // When
    mockMvc.perform(
        get(BASE_URL + "/findMusicalRolesUser/" + musicalBandId + "/" + userId))
        // Then
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Musical roles not found"));
  }

  @Test
  void testAssignMusicalRoles_Valid_Request () throws Exception {
    // Given
    var musicalBandId = UUID.randomUUID();
    var userId = UUID.randomUUID();

    var musicalRoleDto = new MusicalRolesDto(1, "Test", true);
    List<MusicalRolesDto> musicalRolesDtoList = List.of(musicalRoleDto);

    var musicalRolesModel = MusicalRolesModel.builder()
        .id(musicalRoleDto.id())
        .musicalBand(new MusicalBandsModel(musicalBandId))
        .name(musicalRoleDto.name())
        .status(musicalRoleDto.status())
        .build();

    List<MusicalRolesModel> musicalRolesModelList = List.of(musicalRolesModel);

    doNothing().when(musicalRolesUsersService).assignMusicalRolesUser(userId, musicalBandId, musicalRolesModelList);
        
    // When
    mockMvc.perform(
      post(BASE_URL + "/assignMusicalRoles/" + musicalBandId + "/" + userId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(musicalRolesDtoList)))      
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
      .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Musical roles assigned to user"));
  }

  @Test
  void testAssignMusicalRoles_EmptyList() throws Exception {
    // Given
    var musicalBandId = UUID.randomUUID();
    var userId = UUID.randomUUID();

    List<MusicalRolesDto> musicalRolesDtoList = List.of();

    // When
    mockMvc.perform(
        post(BASE_URL + "/assignMusicalRoles/" + musicalBandId + "/" + userId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(musicalRolesDtoList)))
        // Then
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Musical roles list is empty"));
  }
}