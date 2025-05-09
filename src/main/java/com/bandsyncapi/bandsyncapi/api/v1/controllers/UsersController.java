package com.bandsyncapi.bandsyncapi.api.v1.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bandsyncapi.bandsyncapi.api.v1.dto.users.UserLoginPostDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.users.UserRegisterPostDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.users.UserTokenDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.users.UsersDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.users.UsersPutDto;
import com.bandsyncapi.bandsyncapi.api.v1.mappers.UsersMapper;
import com.bandsyncapi.bandsyncapi.api.v1.models.UsersModel;
import com.bandsyncapi.bandsyncapi.api.v1.services.JWTService;
import com.bandsyncapi.bandsyncapi.api.v1.services.UsersService;
import com.bandsyncapi.bandsyncapi.response.ApiResponse;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

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
@RequestMapping(path = "api/v1")
@Slf4j
public class UsersController {

  private static final String USERS_PATH = "users";

  private final UsersService usersService;

  private final JWTService jwtService;

  private UsersMapper usersMapper;

  /**
   * Constructor of the class
   * 
   * @param usersService - Users Service
   * @param usersMapper  - Users mapper
   */
  public UsersController(UsersService usersService, UsersMapper usersMapper, JWTService jwtService) {
    this.usersService = usersService;
    this.jwtService = jwtService;
    this.usersMapper = usersMapper;
  }

  /**
   * Authenticate a user
   * 
   * @param userLoginPostDto - Request body with the user data
   * @return - An ApiResponse object
   */
  @PostMapping("/" + USERS_PATH + "/auth/login")
  public ResponseEntity<ApiResponse<UserTokenDto>> login(@RequestBody UserLoginPostDto  userLoginPostDto) {
    usersService.verify(userLoginPostDto);

    String token = jwtService.generateToken(userLoginPostDto.username());

    var userTokenDto = new UserTokenDto(token);

    log.info("User authenticated successfully: {}", userLoginPostDto.username());  

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ApiResponse<>(true, "User authenticated successfully", userTokenDto, null));
  }

  /**
   * Register a new user
   * 
   * @param userRegisterPostDto - Request body with the user data
   * @return - An ApiResponse object
   */
  @PostMapping("/" + USERS_PATH + "/register")
  public ResponseEntity<ApiResponse<Void>> register(@Valid @RequestBody UserRegisterPostDto userRegisterPostDto) {

    if (!userRegisterPostDto.password().equals(userRegisterPostDto.repeatedPassword())) {
      log.info("Passwords do not match for user: {}", userRegisterPostDto.username());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
      .body(new ApiResponse<>(false, "Passwords do not match", null, null));
    }

    UsersModel usersModel = usersMapper.toModelFromRegisterDto(userRegisterPostDto);
    usersService.register(usersModel);

    log.info("User registered successfully: {}", userRegisterPostDto.username());

    return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true, "User registered successfully", null, null));
  }

  /**
   * join user to a musical band
   * 
   * @param userId        - user id
   * @param musicalBandId - musical band id
   * @return - ApiResponse object
   */
  @PostMapping("/" + USERS_PATH + "/joinUserToMusicalBand/{musicalBandId}/{userId}")
  public ResponseEntity<ApiResponse<Void>> joinUserToMusicalBand(@PathVariable UUID userId,
      @PathVariable UUID musicalBandId) {
    usersService.joinUserToMusicalBand(userId, musicalBandId);

    log.info("User {} joined to musical band {}", userId, musicalBandId);

    return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, "User joined to musical band", null, null));
  }

  /**
   * finds All users that are part of a musical band
   * 
   * @param musicalBandId - musical band id
   * @return ApiResponse object with the users
   */
  @GetMapping("/{musicalBandName}/" + USERS_PATH + "/findAllByMusicalBandId/{musicalBandId}")
  public ResponseEntity<ApiResponse<List<UsersDto>>> findAllByMusicalBandId(@PathVariable String musicalBandName, @PathVariable UUID musicalBandId) {
    List<UsersModel> users = usersService.getAllUsersByMusicalBandId(musicalBandId);

    List<UsersDto> usersResponse = usersMapper.toDtoList(users);

    log.info("Users found: {}", usersResponse);

    return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, "Users found", usersResponse, null));
  }

  /**
   * Find a user by id
   * 
   * @param userId - User id
   * @return - ApiResponse object with the user
   */
  @GetMapping("/" + USERS_PATH + "/findById/{userId}")
  public ResponseEntity<ApiResponse<UsersDto>> findById(@PathVariable UUID userId) {
    UsersModel usersModel = usersService.getById(userId);
    UsersDto response = usersMapper.toDto(usersModel);

    log.info("User found: {}", response);

    return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, "User found", response, null));
  }

  /**
   * update user info
   * 
   * @param id          - user id
   * @param usersPutDto - user data to be updated
   * @return An ApiResponse object
   */
  @PutMapping("/" + USERS_PATH + "/updateUser/{id}")
  public ResponseEntity<ApiResponse<Void>> updateUser(@PathVariable UUID id,
      @Valid @RequestBody UsersPutDto usersPutDto) {
    usersService.updateUser(id, usersPutDto);

    log.info("User updated successfully: {}", usersPutDto);

    return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, "User updated successfully", null, null));
  }

  /**
   * Check if the user exists by email
   * 
   * @param email - email
   * @return An ApiResponse object
   */
  @GetMapping("/" + USERS_PATH + "/existsByEmail")
  public ResponseEntity<ApiResponse<Boolean>> existsByEmail(@RequestParam String email) {
    boolean exists = usersService.existsByEmail(email);

    if (exists) {
      log.info("User found by email: {}", email);
      return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, "User found by email", exists, null));
    }

    log.info("User not found by email: {}", email);
    
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new ApiResponse<>(false, "User not found by email", exists, null));
  }
}
