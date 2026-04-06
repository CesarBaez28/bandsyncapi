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

import com.bandsyncapi.bandsyncapi.api.v1.dto.roles.RolesDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.roles.RolesPermissionsDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.roles.RolesPermissionsPutDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.roles.RolesPostDto;
import com.bandsyncapi.bandsyncapi.api.v1.mappers.RolesMapper;
import com.bandsyncapi.bandsyncapi.api.v1.mappers.UsersMapper;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalBandsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.PermissionsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.RolesModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.RolesPermissionsModel;
import com.bandsyncapi.bandsyncapi.api.v1.services.RolesPermissionsService;
import com.bandsyncapi.bandsyncapi.api.v1.services.RolesService;
import com.bandsyncapi.bandsyncapi.api.v1.services.UsersRolesService;
import com.bandsyncapi.bandsyncapi.config.TestBeansConfig;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(RolesController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(TestBeansConfig.class)
class RolesControllerUnSecuredTest {

  private static final String BASE_URL = "/api/v1/roles";

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private RolesService rolesService;

  @MockitoBean
  private RolesPermissionsService rolesPermissionsService;

  @MockitoBean
  private UsersRolesService usersRolesService;

  @MockitoBean
  private RolesMapper rolesMapper;

  @MockitoBean
  private UsersMapper usersMapper;

  @Test
  void testSave_Valid_Request() throws Exception {
    // Given
    var musicalBandId = UUID.randomUUID();

    var roleModel = RolesModel.builder()
        .id(1)
        .name("Test role")
        .musicalBand(new MusicalBandsModel(musicalBandId))
        .status(true)
        .build();

    var permissionsModel = PermissionsModel.builder()
        .id(1)
        .name("Test permission")
        .status(true)
        .build();

    var postRequest = new RolesPostDto(
        "Test role",
        new MusicalBandsModel(musicalBandId),
        true,
        List.of(permissionsModel));

    var roleDto = new RolesDto(1, "Test role", true);
    var rolesPermissionsModel = new RolesPermissionsModel(roleModel, permissionsModel, true);
    List<RolesPermissionsModel> permissions = List.of(rolesPermissionsModel);

    var rolesPermissionsDto = new RolesPermissionsDto(roleDto, permissions);

    given(rolesService.saveRoleAndPermissions(postRequest)).willReturn(rolesPermissionsDto);

    // When
    mockMvc.perform(
        post(BASE_URL + "/save")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(postRequest)))
        // Then
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Role saved successfully."))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.role.name").value("Test role"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.permissions[0].permission.name").value("Test permission"));
  }

  @Test
  void testSave_Invalid_Request() throws Exception {
    // Given
    var postRequest = new RolesPostDto(
        "Te",
        null,
        true,
        null);

    // When
    mockMvc.perform(
        post(BASE_URL + "/save")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(postRequest)))
        // Then
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.errors.name").value("El nombre debe tener entre 3 y 100 caracteres"));
  }

  @Test
  void testFindAll_Data_Found() throws Exception {
    // Given
    var musicalBandId = UUID.randomUUID();
    var roleModel = RolesModel.builder()
        .id(1)
        .name("Test name")
        .musicalBand(new MusicalBandsModel(musicalBandId))
        .status(true)
        .build();

    var roleDto = new RolesDto(1, "Test name", true);

    List<RolesModel> rolesModelList = List.of(roleModel);
    List<RolesDto> rolesDtoList = List.of(roleDto);

    given(rolesService.findAll()).willReturn(rolesModelList);
    given(rolesMapper.toDtoList(rolesModelList)).willReturn(rolesDtoList);

    // When
    mockMvc.perform(
        get(BASE_URL + "/findAll"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name").value("Test name"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].status").value(true));
  }

  @Test
  void testUpdateRepertoire_Valid_Request() throws Exception {
    // Given
    var permission = PermissionsModel.builder()
        .id(1)
        .name("Test permissions")
        .status(true)
        .build();

    List<PermissionsModel> permissions = List.of(permission);

    var putRequest = new RolesPermissionsPutDto(
        1,
        "New role name",
        permissions);

    // When
    mockMvc.perform(
        put(BASE_URL + "/update")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(putRequest)))
        // Then
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Role updated successfully"));
  }

  @Test
  void testUpdateRepertoire_Invalid_Request() throws Exception {
    // Given
    var putRequest = new RolesPermissionsPutDto(
        1,
        "e",
        null);

    // When
    mockMvc.perform(
        put(BASE_URL + "/update")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(putRequest)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.errors.newName").value("El nombre debe tener entre 3 y 100 caracteres"));
  }
}
