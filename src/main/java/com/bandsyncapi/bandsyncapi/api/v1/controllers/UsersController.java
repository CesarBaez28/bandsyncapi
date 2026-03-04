package com.bandsyncapi.bandsyncapi.api.v1.controllers;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bandsyncapi.bandsyncapi.api.v1.constants.UserPermissions;
import com.bandsyncapi.bandsyncapi.api.v1.dto.users.UserLoginPostDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.users.UserRegisterPostDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.users.UserSessionDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.users.UsersDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.users.UsersPutDto;
import com.bandsyncapi.bandsyncapi.api.v1.mappers.UsersMapper;
import com.bandsyncapi.bandsyncapi.api.v1.models.UsersModel;
import com.bandsyncapi.bandsyncapi.api.v1.services.JWTService;
import com.bandsyncapi.bandsyncapi.api.v1.services.UsersMusicalBandsService;
import com.bandsyncapi.bandsyncapi.api.v1.services.UsersRolesService;
import com.bandsyncapi.bandsyncapi.api.v1.services.UsersService;
import com.bandsyncapi.bandsyncapi.response.ApiResponse;
import com.bandsyncapi.bandsyncapi.response.PagedData;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;

/**
 * This is the controller to handle requests for the users table.
 */
@RestController
@RequestMapping(path = "api/v1")
@Slf4j
public class UsersController {

  private static final int PAGE_SIZE = 15;

  private final UsersService usersService;

  private final UsersMusicalBandsService usersMusicalBandsService;

  private final UsersRolesService usersRolesService;

  private final JWTService jwtService;

  private UsersMapper usersMapper;

  /**
   * Constructor of the class
   * 
   * @param usersService             - Users Servic
   * @param usersMusicalBandsService - UsersMusicalBands Service
   * @param jwtService               - JWT Service
   * @param usersMapper              - Users mapper
   * @param usersRolesService        - UsersRoles Service
   */
  public UsersController(UsersService usersService, UsersMusicalBandsService usersMusicalBandsService,
      UsersRolesService usersRolesService,
      UsersMapper usersMapper, JWTService jwtService) {
    this.usersService = usersService;
    this.usersMusicalBandsService = usersMusicalBandsService;
    this.usersRolesService = usersRolesService;
    this.jwtService = jwtService;
    this.usersMapper = usersMapper;
  }

  /**
   * Authenticate a user
   * 
   * @param userLoginPostDto - Request body with the user data
   * @return - An ApiResponse object
   */
  @PostMapping("/users/auth/login")
  public ResponseEntity<ApiResponse<UserSessionDto>> login(@RequestBody UserLoginPostDto userLoginPostDto) {
    usersService.verify(userLoginPostDto);

    log.info("User authenticated successfully: {}", userLoginPostDto.username());

    String token = jwtService.generateToken(userLoginPostDto.username());

    log.info("Generated token for user: {}", userLoginPostDto.username());

    UsersModel userModel = usersService.getByUsername(userLoginPostDto.username());  

    UserSessionDto userSessionDto = usersMapper.toSessionDto(userModel, token);

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ApiResponse<>(true, "User authenticated successfully", userSessionDto, null));
  }

  /**
   * Register a new user
   * 
   * @param userRegisterPostDto - Request body with the user data
   * @return - An ApiResponse object
   */
  @PostMapping("/users/register")
  public ResponseEntity<ApiResponse<Void>> register(@Valid @RequestBody UserRegisterPostDto userRegisterPostDto) {

    if (!userRegisterPostDto.password().equals(userRegisterPostDto.repeatedPassword())) {
      log.info("Passwords do not match for user: {}", userRegisterPostDto.username());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(new ApiResponse<>(false, "Passwords do not match", null, null));
    }

    UsersModel usersModel = usersMapper.toModelFromRegisterDto(userRegisterPostDto);
    usersService.register(usersModel);

    log.info("User registered successfully: {}", userRegisterPostDto.username());

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new ApiResponse<>(true, "User registered successfully", null, null));
  }

  /**
   * Register a user from an invitation
   * 
   * @param userRegisterPostDto - User registration data
   * @param token               - invitation token
   * @return - An ApiResponse object
   */
  @PostMapping("/users/register/{token}")
  public ResponseEntity<ApiResponse<Void>> registerFromToken(
      @Valid @RequestBody UserRegisterPostDto userRegisterPostDto, @PathVariable String token) {

    log.info("Registering user from invitation with token: {}", token);

    if (!userRegisterPostDto.password().equals(userRegisterPostDto.repeatedPassword())) {
      log.info("Passwords do not match for user: {}", userRegisterPostDto.username());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(new ApiResponse<>(false, "Passwords do not match", null, null));
    }

    UsersModel usersModel = usersMapper.toModelFromRegisterDto(userRegisterPostDto);
    usersService.registerFromInvitation(usersModel, token);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new ApiResponse<>(true, "User registered successfully from invitation", null, null));
  }

  /**
   * join user to a musical band
   * 
   * @param userId        - user id
   * @param musicalBandId - musical band id
   * @return - ApiResponse object
   */
  @PostMapping("/users/joinUserToMusicalBand/{musicalBandId}/{userId}")
  @PreAuthorize("hasRole('" + UserPermissions.ADD_MEMBER + "')")
  public ResponseEntity<ApiResponse<Void>> joinUserToMusicalBand(@PathVariable UUID userId,
      @PathVariable UUID musicalBandId) {

    usersService.joinUserToMusicalBand(userId, musicalBandId);

    log.info("User {} joined to musical band {}", userId, musicalBandId);

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ApiResponse<>(true, "User joined to musical band", null, null));
  }

  /**
   * leave user from a musical band
   * 
   * @param userId        - user id
   * @param musicalBandId - musical band id
   * @return - ApiResponse object
   */
  @PostMapping("/users/leaveMusicalBand/{musicalBandId}/{userId}")
  @PreAuthorize("hasRole('" + UserPermissions.DELETE_MEMBER + "')")
  public ResponseEntity<ApiResponse<Void>> leaveMusicalBand(@PathVariable UUID userId,
      @PathVariable UUID musicalBandId) {

    usersMusicalBandsService.deleteByUserIdAndMusicalBandId(userId, musicalBandId);

    usersRolesService.deleteByUserIdAndMusicalBandId(userId, musicalBandId);

    log.info("User {} left musical band {}", userId, musicalBandId);

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ApiResponse<>(true, "User left musical band", null, null));
  }

  /**
   * finds All users that are part of a musical band
   * 
   * @param musicalBandId - musical band id
   * @return ApiResponse object with the users
   */
  @GetMapping("/users/findAllByMusicalBandId/{musicalBandId}")
  public ResponseEntity<ApiResponse<List<UsersDto>>> findAllByMusicalBandId(@PathVariable UUID musicalBandId) {
    List<UsersModel> users = usersService.getAllUsersByMusicalBandId(musicalBandId);

    List<UsersDto> usersResponse = usersMapper.toDtoList(users);

    if (usersResponse.isEmpty()) {
      return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, "Users not found", usersResponse, null));
    }

    log.info("Users found: {}", usersResponse);

    return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, "Users found", usersResponse, null));
  }

  /**
   * finds users by musical band id and by
   * username, email, firstname, lastname and phone number
   * 
   * @param musicalBandId - musical band id
   * @param query         - search term
   * @param page          - page number
   * @return A Page of type UsersDto
   */
  @GetMapping("/users/find/{musicalBandId}")
  @PreAuthorize("hasRole('" + UserPermissions.VIEW_MEMBERS + "')")
  public ResponseEntity<ApiResponse<PagedData<UsersDto>>> find(
      @PathVariable UUID musicalBandId,
      @RequestParam String query,
      @RequestParam(defaultValue = "0") int page) {

    page = Math.max(page - 1, 0); // convert to zero-based index and ensure non-negative

    Page<UsersModel> usersPage = usersService.find(musicalBandId, query, page, PAGE_SIZE);

    List<UsersDto> userDtoList = usersMapper.toDtoList(usersPage.getContent());

    PagedData<UsersDto> pagedData = new PagedData<>(userDtoList, usersPage);

    log.info("Repertoires found by musical band id: {} and term: {}", musicalBandId, query);

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ApiResponse<>(true, "Repertoires found successfully", pagedData, null));
  }

  /**
   * Find a user by id
   * 
   * @param userId - User id
   * @return - ApiResponse object with the user
   */
  @GetMapping("/users/findById/{userId}")
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
  @PutMapping(path = "/users/updateUser/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ApiResponse<UsersPutDto>> updateUser(@PathVariable UUID id,
      @Valid @RequestPart("user") UsersPutDto usersPutDto,
      @RequestPart(value = "image", required = false) MultipartFile imageFile) throws IOException {

    UsersPutDto result = usersService.updateUser(id, usersPutDto, imageFile);

    log.info("User updated successfully: {}", result);

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ApiResponse<>(true, "User updated successfully", result, null));
  }

  /**
   * Check if the user exists by email
   * 
   * @param email - email
   * @return An ApiResponse object
   */
  @GetMapping("/users/existsByEmail")
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
