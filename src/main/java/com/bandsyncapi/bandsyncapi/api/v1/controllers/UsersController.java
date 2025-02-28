package com.bandsyncapi.bandsyncapi.api.v1.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bandsyncapi.bandsyncapi.api.v1.dto.users.UsersDto;
import com.bandsyncapi.bandsyncapi.api.v1.mappers.UsersMapper;
import com.bandsyncapi.bandsyncapi.api.v1.models.UsersModel;
import com.bandsyncapi.bandsyncapi.api.v1.services.UsersService;
import com.bandsyncapi.bandsyncapi.response.ApiResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * This is the controller to handle requests for the users table.
 */
@Controller
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

    if (users.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, "No se encontraron datos", null, null));
    }

    List<UsersDto> usersResponse = usersMapper.toDtoList(users);

    return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, "Datos encontrados", usersResponse, null));
  }
}
