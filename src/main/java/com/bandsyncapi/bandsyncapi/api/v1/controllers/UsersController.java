package com.bandsyncapi.bandsyncapi.api.v1.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bandsyncapi.bandsyncapi.api.v1.dto.users.UserRegisterPostDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.users.UsersDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.users.UsersPutDto;
import com.bandsyncapi.bandsyncapi.api.v1.mappers.UsersMapper;
import com.bandsyncapi.bandsyncapi.api.v1.models.UsersModel;
import com.bandsyncapi.bandsyncapi.api.v1.services.UsersService;
import com.bandsyncapi.bandsyncapi.response.ApiResponse;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * This is the controller to handle requests for the users table.
 */
@RestController
@RequestMapping(path = "api/v1/users")
public class UsersController {

  private final UsersService usersService;

  private UsersMapper usersMapper;

  /**
   * Contructor
   * 
   * @param usersService - Users Service
   * @param usersMapper  - Users mapper
   */
  public UsersController(UsersService usersService, UsersMapper usersMapper) {
    this.usersService = usersService;
    this.usersMapper = usersMapper;
  }

  /**
   * Register a new user
   * 
   * @param userRegisterPostDto - Request body with the user data
   * @return - An ApiResponse object
   */
  @PostMapping("/register")
  public ResponseEntity<ApiResponse<Void>> register(@Valid @RequestBody UserRegisterPostDto userRegisterPostDto) {

    if (!userRegisterPostDto.password().equals(userRegisterPostDto.repeatedPassword())) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(new ApiResponse<>(false, "Las contraseñas no coinciden.", null, null));
    }

    UsersModel usersModel = usersMapper.toModelFromRegisterDto(userRegisterPostDto);
    usersService.register(usersModel);

    return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true, "Usuario registrado.", null, null));
  }

  /**
   * join user to a musical band
   * 
   * @param userId - user id
   * @param musicalBandId - musical band id
   * @return - ApiResponse object
   */
  @PostMapping("/joinUserToMusicalBand/{userId}/{musicalBandId}")
  public ResponseEntity<ApiResponse<Void>> joinUserToMusicalBand(@PathVariable UUID userId,
      @PathVariable UUID musicalBandId) {
    usersService.joinUserToMusicalBand(userId, musicalBandId);

    return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, "Usuario unido a la banda.", null, null));
  }

  /**
   * finds All users that are part of a musical band
   * 
   * @param musicalBandId - musical band id
   * @return ApiResponse object with the users
   */
  @GetMapping("/findAllByMusicalBandId/{musicalBandId}")
  public ResponseEntity<ApiResponse<List<UsersDto>>> findAllByMusicalBandId(@PathVariable UUID musicalBandId) {
    List<UsersModel> users = usersService.getAllUsersByMusicalBandId(musicalBandId);

    List<UsersDto> usersResponse = usersMapper.toDtoList(users);

    return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, "Datos encontrados", usersResponse, null));
  }

  /**
   * Find a user by id
   * 
   * @param userId - User id
   * @return - ApiResponse object with the user
   */
  @GetMapping("/findById/{userId}")
  public ResponseEntity<ApiResponse<UsersDto>> findById(@PathVariable UUID userId) {
    UsersModel usersModel = usersService.getById(userId);
    UsersDto response = usersMapper.toDto(usersModel);

    return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, "Usuario encontrado", response, null));
  }

  /**
   * update user info
   * 
   * @param id          - user id
   * @param usersPutDto - user data to be updated
   * @return An ApiResponse object
   */
  @PutMapping("/updateUser/{id}")
  public ResponseEntity<ApiResponse<Void>> updateUser(@PathVariable UUID id,
      @Valid @RequestBody UsersPutDto usersPutDto) {
    usersService.updateUser(id, usersPutDto);

    return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, "Datos actualizados", null, null));
  }

  /**
   * Check if the user exists by email
   * 
   * @param email - email
   * @return An ApiResponse object
   */
  @GetMapping("/existsByEmail")
  public ResponseEntity<ApiResponse<Boolean>> existsByEmail(@RequestParam String email) {
    boolean exists = usersService.existsByEmail(email);

    if (exists) {
      return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, "Usuario encontrado", exists, null));
    }

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, "Usuario no encontrado", exists, null));
  }
}
