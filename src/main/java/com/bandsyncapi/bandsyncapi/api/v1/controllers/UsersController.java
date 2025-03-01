package com.bandsyncapi.bandsyncapi.api.v1.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

  @PutMapping("updateUser/{id}")
  public ResponseEntity<ApiResponse<Void>> updateUser(@PathVariable UUID id, @Valid @RequestBody UsersPutDto usersPutDto) {
    usersService.updateUser(id, usersPutDto);

    return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, "Datos actualizados", null, null));
  }
}
