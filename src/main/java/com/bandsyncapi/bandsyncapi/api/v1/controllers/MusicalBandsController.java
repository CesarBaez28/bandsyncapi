package com.bandsyncapi.bandsyncapi.api.v1.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bandsyncapi.bandsyncapi.api.v1.constants.UserPermissions;
import com.bandsyncapi.bandsyncapi.api.v1.dto.invitations.InviteRequestDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.musicalbands.MusicalBandPutDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.musicalbands.MusicalBandsDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.musicalbands.MusicalBandsPostDto;
import com.bandsyncapi.bandsyncapi.api.v1.models.UsersModel;
import com.bandsyncapi.bandsyncapi.api.v1.services.InvitationsService;
import com.bandsyncapi.bandsyncapi.api.v1.services.MusicalBandDeletionService;
import com.bandsyncapi.bandsyncapi.api.v1.services.MusicalBandsService;
import com.bandsyncapi.bandsyncapi.api.v1.services.UsersMusicalBandsService;
import com.bandsyncapi.bandsyncapi.response.ApiResponse;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * This is the controller to handle requests for the MusicalBandsModel.
 */
@RestController
@RequestMapping(path = "api/v1/musical-bands")
@Slf4j
public class MusicalBandsController {

  private final MusicalBandsService musicalBandsService;

  private final UsersMusicalBandsService usersMusicalBandsService;

  private final InvitationsService invitationsService;

  private final MusicalBandDeletionService musicalBandDeletionService;

  /**
   * Constructor for the MusicalBandsController class.
   * 
   * @param musicalBandsService        - Service with methods for performing CRUD
   *                                   operations on the musical_bands table.
   * @param usersMusicalBandsService   - Service with methods for performing CRUD
   *                                   operations on the users_musical_bands
   *                                   table.
   * @param invitationsService         - Service to send invitation to join to a
   *                                   musical band
   * @param musicalBandDeletionService - Service to delete a musical band
   */
  public MusicalBandsController(MusicalBandsService musicalBandsService,
      UsersMusicalBandsService usersMusicalBandsService, InvitationsService invitationsService,
      MusicalBandDeletionService musicalBandDeletionService) {
    this.musicalBandsService = musicalBandsService;
    this.usersMusicalBandsService = usersMusicalBandsService;
    this.invitationsService = invitationsService;
    this.musicalBandDeletionService = musicalBandDeletionService;
  }

  /**
   * Saves a musical band to the database.
   * 
   * @param musicalBandsPostDto - Musical band to be saved.
   * @return - The saved musical band.
   */
  @PostMapping(path = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ApiResponse<MusicalBandsDto>> saveMusicalBand(
      @Valid @RequestPart("musicalBand") MusicalBandsPostDto musicalBandsPostDto,
      @RequestPart(value = "image", required = false) MultipartFile imageFile) throws IOException {

    MusicalBandsDto saved = musicalBandsService.registerMusicalBand(musicalBandsPostDto, imageFile);

    log.info("Musical band registered successfully: {}", saved);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new ApiResponse<>(true, "Banda musical registrada correctamente", saved, null));
  }

  /**
   * Update musical band info
   * 
   * @param id                - musical band id
   * @param musicalBandPutDto - musical bad info to be uptaded
   * @param imageFile         - new logo file
   * @return - a MusicalBandsDto object with the new values
   * @throws IOException
   */
  @PutMapping(path = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @PreAuthorize("hasRole('" + UserPermissions.UPDATE_BAND + "')")
  public ResponseEntity<ApiResponse<MusicalBandPutDto>> update(
      @PathVariable UUID id,
      @Valid @RequestPart("musicalBand") MusicalBandPutDto musicalBandPutDto,
      @RequestPart(value = "image", required = false) MultipartFile imageFile) throws IOException {

    MusicalBandPutDto result = musicalBandsService.update(id, musicalBandPutDto, imageFile);

    log.info("Musical band successfully updated");

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ApiResponse<>(true, "Musical Band successfully updated", result, null));
  }

  /**
   * find musical band by od
   * 
   * @param musicalBandId - musical band id
   * @return - An ApiResponse Object containing the list of MusicalBandsDto
   */
  @GetMapping("/findById/{musicalBandId}")
  public ResponseEntity<ApiResponse<MusicalBandsDto>> findById(@PathVariable UUID musicalBandId) {
    MusicalBandsDto response = musicalBandsService.findById(musicalBandId);

    log.info("Musical band found successfully");

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ApiResponse<>(true, "Musical band found successfully", response, null));
  }

  /**
   * Find all the bands a user is a part of
   * 
   * @param userId - user id
   * @return - An ApiResponse Object containing the list of MusicalBandsDto
   */
  @GetMapping("/findByUserId/{userId}")
  public ResponseEntity<ApiResponse<List<MusicalBandsDto>>> findByUserId(@PathVariable UUID userId) {
    var user = new UsersModel(userId);
    List<MusicalBandsDto> response = usersMusicalBandsService.findByUser(user);

    log.info("Musical bands found successfully");

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ApiResponse<>(true, "Musical bands found successfully", response, null));
  }

  /**
   * find a musical band by hyphenatedName
   * 
   * @param hyphenatedName - hyphenatedName of the musical band
   * @return - The found musical band
   */
  @GetMapping("/findByHyphenatedName/{hyphenatedName}")
  public ResponseEntity<ApiResponse<MusicalBandsDto>> findByHyphenatedName(@PathVariable String hyphenatedName) {

    MusicalBandsDto musicalBand = musicalBandsService.findByHyphenatedName(hyphenatedName);

    log.info("Musical band found successfully by hyphenatedName: {}", hyphenatedName);

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ApiResponse<>(true, "Musical band found successfully.", musicalBand, null));
  }

  /**
   * send an invitation to join to a musical band
   * 
   * @param musicalBandId - musical band id
   * @param inviteRequest - user who send the invitation
   * @return - An ApiResponse object
   * @throws MessagingException
   */
  @PostMapping("/{musicalBandId}/invite")
  public ResponseEntity<ApiResponse<Void>> invite(@PathVariable UUID musicalBandId,
      @Valid @RequestBody InviteRequestDto inviteRequest) throws MessagingException {

    invitationsService.sendInvitation(musicalBandId, inviteRequest.email(), inviteRequest.invitedBy());

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ApiResponse<>(true, "Invitation send successfully", null, null));
  }

  /**
   * Deletes a musical band
   * 
   * @param id - musical band id
   * @return An ApiResponse object
   */
  @DeleteMapping("/delete/{id}")
  @PreAuthorize("hasRole('" + UserPermissions.DELETE_BAND + "')")
  public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
    musicalBandDeletionService.deleteMusuicalBand(id);

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ApiResponse<>(true, "Musical band deleted successfully", null, null));
  }
}
