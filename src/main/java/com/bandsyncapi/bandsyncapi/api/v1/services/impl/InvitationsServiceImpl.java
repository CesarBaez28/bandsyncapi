package com.bandsyncapi.bandsyncapi.api.v1.services.impl;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bandsyncapi.bandsyncapi.api.v1.enums.InvitationStatus;
import com.bandsyncapi.bandsyncapi.api.v1.models.InvitationsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalBandsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.UsersModel;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.InvitationsRepository;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.MusicalBandsRepository;
import com.bandsyncapi.bandsyncapi.api.v1.services.EmailService;
import com.bandsyncapi.bandsyncapi.api.v1.services.InvitationsService;
import com.bandsyncapi.bandsyncapi.api.v1.services.JWTService;
import com.bandsyncapi.bandsyncapi.api.v1.services.UsersService;
import com.bandsyncapi.bandsyncapi.exceptions.InvitationException;

import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import lombok.var;
import lombok.extern.slf4j.Slf4j;

/**
 * InvitationsService implementation
 */
@Service
@Slf4j
public class InvitationsServiceImpl implements InvitationsService {

  private final JWTService jwtService;

  private final UsersService usersService;

  private final InvitationsRepository invitationsRepository;

  private final MusicalBandsRepository musicalBandsRepository;

  private final EmailService emailService;

  private static final long DAYS_EXPIRES = 7;

  private static final String INVITATION_PATH = "invitation";

  @Value("${fronted.url}")
  private String frontedUrl;

  /**
   * Constructor
   * 
   * @param jwtService             - Jwt service
   * @param usersService           - UsersService
   * @param invitationsRepository  - InvitationsRepository
   * @param musicalBandsRepository - MusicalBandsRepository
   * @param emailService           - EmailService
   */
  public InvitationsServiceImpl(JWTService jwtService, UsersService usersService,
      InvitationsRepository invitationsRepository,
      MusicalBandsRepository musicalBandsRepository, EmailService emailService) {
    this.jwtService = jwtService;
    this.usersService = usersService;
    this.invitationsRepository = invitationsRepository;
    this.musicalBandsRepository = musicalBandsRepository;
    this.emailService = emailService;
  }

  @Override
  public InvitationsModel save(InvitationsModel invitation) {
    log.info("Saving invitation");
    return invitationsRepository.save(invitation);
  }

  @Override
  public InvitationsModel findByToken(String token) {
    log.info("Finding invitation by token");
    return invitationsRepository.findByToken(token)
        .orElseThrow(() -> new EntityNotFoundException("Invitation not found"));
  }

  @Override
  public void acceptInvitation(String token, UUID userId) {
    log.info("Accepting invitation");
    InvitationsModel invitation = validateInvitationToken(token);

    usersService.joinUserToMusicalBand(userId, invitation.getMusicalBand().getId());

    invitation.setStatus(InvitationStatus.ACCEPTED);
    invitation.setExpiresAt(LocalDateTime.now());
    invitationsRepository.save(invitation);

    log.info("Invitation accepted successfully");
  }

  @Override
  public InvitationsModel validateInvitationToken(String token) {
    log.info("Validating invitation");

    InvitationsModel invitation = invitationsRepository.findByToken(token)
        .orElseThrow(() -> new EntityNotFoundException("Invitation not found"));

    if (invitation.getStatus() != InvitationStatus.PENDING) {
      log.error("This invitation has already been used");
      throw new InvitationException("This invitation has already been used.");
    }

    if (invitation.getExpiresAt().isBefore(LocalDateTime.now())) {
      log.error("This invitation is expired");
      invitation.setStatus(InvitationStatus.EXPIRED);
      invitationsRepository.save(invitation);
      throw new InvitationException("This invitation is expired");
    }

    log.info("Invitation is ok");

    return invitation;
  }

  @Override
  public void sendInvitation(UUID musicalBandId, String email, UsersModel invitedBy) throws MessagingException {
    log.info("Sending invitation to: {}", email);

    // Check if the band exists
    MusicalBandsModel band = musicalBandsRepository.findById(musicalBandId)
        .orElseThrow(() -> new IllegalArgumentException("Musical Band not found"));

    // Claims for jwt token
    Map<String, Object> claims = Map.of(
        "bandId", musicalBandId.toString(),
        "bandName", band.getName(),
        "email", email);

    String token = jwtService.generateInvitationToken(claims, LocalDateTime.now().plusDays(DAYS_EXPIRES));

    log.info("Token generated for invitation");

    var expirationDate = LocalDateTime.now().plusDays(DAYS_EXPIRES);

    invitedBy = usersService.getById(invitedBy.getId());

    var invitation = InvitationsModel.builder()
        .email(email)
        .musicalBand(band)
        .token(token)
        .invitedBy(invitedBy)
        .createdAt(LocalDateTime.now())
        .expiresAt(expirationDate)
        .build();

    invitationsRepository.save(invitation);

    log.info("Invitation info saved in DB successfully");

    String invitationLink = frontedUrl + "/" + INVITATION_PATH
        + "?token="
        + token;

    emailService.sendInvitationEmail(email, band.getName(), invitedBy.getFirstName() + " " + invitedBy.getLastName(),
        invitationLink, expirationDate);
  }
}