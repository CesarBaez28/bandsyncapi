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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.bandsyncapi.bandsyncapi.api.v1.dto.users.UserLoginPostDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.users.UserRegisterPostDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.users.UsersDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.users.UsersPutDto;
import com.bandsyncapi.bandsyncapi.api.v1.mappers.UsersMapper;
import com.bandsyncapi.bandsyncapi.api.v1.models.UsersModel;
import com.bandsyncapi.bandsyncapi.api.v1.services.JWTService;
import com.bandsyncapi.bandsyncapi.api.v1.services.UsersService;
import com.bandsyncapi.bandsyncapi.config.TestBeansConfig;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(UsersController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(TestBeansConfig.class)
class UsersControllerUnSecuredTest {

  private static final String BASE_URL = "/api/v1/users";

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  protected ObjectMapper objectMapper;

  @MockitoBean
  private UsersService usersService;

  @MockitoBean
  private UsersMapper usersMapper;

  @MockitoBean
  private JWTService jwtService;

  @Test
  void testLogin_Valid_Authentication() throws Exception {
    // Given
    var postRequest = new UserLoginPostDto("Test username", "test password");
    var token = "token";

    given(usersService.verify(postRequest)).willReturn(true);
    given(jwtService.generateToken(postRequest.username())).willReturn(token);

    // When
    mockMvc.perform(
        post(BASE_URL + "/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(postRequest)))
        // Then
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User authenticated successfully"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.token").value(token));
  }

  @Test
  void testLogin_Bad_Credentials() throws Exception {
    // Given
    var postRequest = new UserLoginPostDto("Invalid username", "Invalid password");

    given(usersService.verify(postRequest)).willThrow(BadCredentialsException.class);

    // When
    mockMvc.perform(
        post(BASE_URL + "/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(postRequest)))
        // Then
        .andExpect(MockMvcResultMatchers.status().isUnauthorized())
        .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Bad credentials."));
  }

  @Test
  void testRegister_Valid_Request() throws Exception {
    // Given
    var userId = UUID.randomUUID();
    var postRequest = new UserRegisterPostDto(
        "testUsername",
        "cesarbaez@gmail.com",
        "CesarBaez28#",
        "CesarBaez28#");

    var userModel = UsersModel.builder()
        .id(userId)
        .username(postRequest.username())
        .password(postRequest.password())
        .email(postRequest.email())
        .firstName("test first name")
        .lastName("test last name")
        .phone("8983563234")
        .photo("http://amazon.com")
        .status(true)
        .build();

    given(usersMapper.toModelFromRegisterDto(postRequest)).willReturn(userModel);

    // When
    mockMvc.perform(
        post(BASE_URL + "/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(postRequest)))
        // Then
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User registered successfully"));
  }

  @Test
  void testRegister_Invalid_Request() throws Exception {
    // Given
    var postRequest = new UserRegisterPostDto(
        "C",
        "cesarbaez@.com",
        "CesarBaez",
        "CesarBaez");

    // When
    mockMvc.perform(
        post(BASE_URL + "/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(postRequest)))
        // Then
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
        .andExpect(MockMvcResultMatchers.jsonPath("$.errors.username")
            .value("El nombre de usuario debe tener al menos 3 caracteres"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.errors.email").value("El correo electrónico no es válido"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.errors.password").value(
            "La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula, un número y un caracter especial"));
  }

  @Test
  void testRegister_Password_Not_Match() throws Exception {
    // Given
    var incorrectPassword = "incorrectPassword";
    var postRequest = new UserRegisterPostDto(
        "testUsername",
        "cesarbaez@gmail.com",
        "CesarBaez28#",
        incorrectPassword);

    // When
    mockMvc.perform(
        post(BASE_URL + "/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(postRequest)))
        // Then
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Passwords do not match"));
  }

  @Test
  void testJoinUserToMusicalBand_Valid_Request() throws Exception {
    // Given
    var userId = UUID.randomUUID();
    var musicalBandId = UUID.randomUUID();

    // When
    mockMvc.perform(
        post(BASE_URL + "/joinUserToMusicalBand/" + musicalBandId + "/" + userId))
        // Then
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User joined to musical band"));
  }

  @Test
  void testFindAllByMusicalBandId_Valid_Request() throws Exception {
    // Given
    var musicalBandId = UUID.randomUUID();
    var userId = UUID.randomUUID();

    var userModel = UsersModel.builder()
        .id(userId)
        .username("username")
        .password("cesarBaez23$A#s")
        .email("cesar@gmail.com")
        .firstName("test first name")
        .lastName("test last name")
        .phone("8983563234")
        .photo("http://amazon.com")
        .status(true)
        .build();

    var userDto = new UsersDto(
        userModel.getId(),
        userModel.getUsername(),
        userModel.getEmail(),
        userModel.getFirstName(),
        userModel.getLastName(),
        userModel.getPhone(),
        userModel.getPhoto(),
        userModel.getStatus());

    List<UsersModel> usersModelList = List.of(userModel);
    List<UsersDto> usersDtoList = List.of(userDto);

    given(usersService.getAllUsersByMusicalBandId(musicalBandId)).willReturn(usersModelList);
    given(usersMapper.toDtoList(usersModelList)).willReturn(usersDtoList);

    // When
    mockMvc.perform(
        get(BASE_URL + "/findAllByMusicalBandId/" + musicalBandId))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Users found"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].username").value(userDto.username()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].email").value(userDto.email()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].firstName").value(userDto.firstName()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].lastName").value(userDto.lastName()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].phone").value(userDto.phone()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].photo").value(userDto.photo()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].status").value(userDto.status()));
  }

  @Test
  void testFindById_Valid_Request() throws Exception {
    // Given
    var userId = UUID.randomUUID();

    var userModel = UsersModel.builder()
        .id(userId)
        .username("username")
        .password("cesarBaez23$A#s")
        .email("cesar@gmail.com")
        .firstName("test first name")
        .lastName("test last name")
        .phone("8983563234")
        .photo("http://amazon.com")
        .status(true)
        .build();

    var userDto = new UsersDto(
        userModel.getId(),
        userModel.getUsername(),
        userModel.getEmail(),
        userModel.getFirstName(),
        userModel.getLastName(),
        userModel.getPhone(),
        userModel.getPhoto(),
        userModel.getStatus());

    given(usersService.getById(userId)).willReturn(userModel);
    given(usersMapper.toDto(userModel)).willReturn(userDto);

    // When
    mockMvc.perform(
        get(BASE_URL + "/findById/" + userId))
        // Then
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User found"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.username").value(userDto.username()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.email").value(userDto.email()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.firstName").value(userDto.firstName()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.lastName").value(userDto.lastName()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.phone").value(userDto.phone()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.photo").value(userDto.photo()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.status").value(userDto.status()));
  }

  @Test
  void testUpdateUser_Valid_Request() throws Exception {
    // Given
    var userId = UUID.randomUUID();
    var putRequest = new UsersPutDto(
        "CesarBaez87",
        "CesarBaez@gmail.com",
        "Cesar",
        "Baez",
        "89798453542",
        "http://localhost",
        true);

    // When
    mockMvc.perform(
        put(BASE_URL + "/updateUser/" + userId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(putRequest)))
        // Then
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User updated successfully"));
  }

  @Test
  void testUpdateUser_Invalid_Request() throws Exception {
    // Given
    var userId = UUID.randomUUID();
    var putRequest = new UsersPutDto(
        "cs",
        "CesarBaez",
        "Cesar",
        "Baez",
        "89798453542",
        "http://localhost",
        true);

    // When
    mockMvc.perform(
        put(BASE_URL + "/updateUser/" + userId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(putRequest)))
        // Then
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
        .andExpect(MockMvcResultMatchers.jsonPath("$.errors.username")
            .value("El nombre de usuario debe tener entre 3 y 100 caracteres."))
        .andExpect(MockMvcResultMatchers.jsonPath("$.errors.email").value("Correo electrónico no válido"));
  }

  @Test
  void testExistsByEmail_User_Found() throws Exception {
    // Given
    var email = "CesarBaez@gmail.com";

    given(usersService.existsByEmail(email)).willReturn(true);

    // When
    mockMvc.perform(
        get(BASE_URL + "/existsByEmail?email=" + email))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User found by email"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data").value(true));
  }

  @Test
  void testExistsByEmail_User_Not_Found() throws Exception {
    // Given
    var email = "CesarBaez@gmail.com";

    given(usersService.existsByEmail(email)).willReturn(false);

    // When
    mockMvc.perform(
        get(BASE_URL + "/existsByEmail?email=" + email))
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User not found by email"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data").value(false));
  }
}