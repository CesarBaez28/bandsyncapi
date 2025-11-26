package com.bandsyncapi.bandsyncapi.api.v1.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bandsyncapi.bandsyncapi.api.v1.dto.invitations.AcceptInvitationDto;
import com.bandsyncapi.bandsyncapi.api.v1.models.UsersModel;
import com.bandsyncapi.bandsyncapi.api.v1.services.InvitationsService;
import com.bandsyncapi.bandsyncapi.api.v1.services.JWTService;
import com.bandsyncapi.bandsyncapi.api.v1.services.UsersService;
import com.bandsyncapi.bandsyncapi.response.ApiResponse;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Invitations controller
 */
@RestController
@RequestMapping(path = "api/v1/invitations")
@Slf4j
public class InvitationsController {

  private final InvitationsService invitationsService;

  private final JWTService jwtService;

  private final UsersService usersService;

  /**
   * Constructor
   * 
   * @param invitationsService - InvitationsService
   * @param jwtService         - JWTService
   * @param usersService       - UsersService
   */
  public InvitationsController(InvitationsService invitationsService, JWTService jwtService,
      UsersService usersService) {
    this.invitationsService = invitationsService;
    this.jwtService = jwtService;
    this.usersService = usersService;
  }

  /**
   * Accept an invitation
   * 
   * @param token - token
   * @return An ApiResponse object of type AcceptedInvitationDto
   */
  @PostMapping("/accept/{token}")
  public ResponseEntity<ApiResponse<AcceptInvitationDto>> accept(@PathVariable String token) {
    log.info("proceeding process to accept invitation...");

    Claims claims = jwtService.extractAllClaims(token);

    String email = (String) claims.get("email");

    String bandName = (String) claims.get("bandName");

    Optional<UsersModel> existingUser = usersService.findByEmail(email);

    boolean userExist = existingUser.isPresent();

    var response = AcceptInvitationDto.builder()
        .email(email)
        .userExist(userExist)
        .bandName(bandName)
        .build();

    if (!userExist) {
      log.info("We could not accept the invitation because there is no user with the email: {}", email);
      return ResponseEntity.status(HttpStatus.OK)
          .body(new ApiResponse<>(false,
              "We could not accept the invitation because there is no user with the email: " + email, response, null));
    }

    invitationsService.acceptInvitation(token, existingUser.get().getId());

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ApiResponse<>(true, "Invitation accepted successfully", response, null));
  }

}
