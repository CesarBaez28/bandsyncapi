package com.bandsyncapi.bandsyncapi.api.v1.services.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bandsyncapi.bandsyncapi.api.v1.repositories.EventsRepository;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.InvitationsRepository;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.MusicalGenresRepository;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.MusicalRolesRepository;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.MusicalRolesUsersRepository;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.UsersMusicalBandsRepository;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.UsersRolesRepository;
import com.bandsyncapi.bandsyncapi.api.v1.services.ArtistsService;
import com.bandsyncapi.bandsyncapi.api.v1.services.MusicalBandDeletionService;
import com.bandsyncapi.bandsyncapi.api.v1.services.MusicalBandsService;
import com.bandsyncapi.bandsyncapi.api.v1.services.RepertoiresService;
import com.bandsyncapi.bandsyncapi.api.v1.services.RolesService;
import com.bandsyncapi.bandsyncapi.api.v1.services.SongsService;

import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of MusicalBandDeletionService
 */
@Service
@Slf4j
public class MusicalBandDeletionServiceImpl implements MusicalBandDeletionService {

  private final EventsRepository eventsRepository;

  private final RepertoiresService repertoiresService;

  private final InvitationsRepository invitationsRepository;

  private final SongsService songsService;

  private final ArtistsService artistsService;

  private final MusicalGenresRepository musicalGenresRepository;

  private final UsersRolesRepository usersRolesRepository;

  private final UsersMusicalBandsRepository usersMusicalBandsRepository;

  private final MusicalRolesUsersRepository musicalRolesUsersRepository;

  private final MusicalRolesRepository musicalRolesRepository;

  private final RolesService rolesService;

  private final MusicalBandsService musicalBandsService;

  /**
   * Constructor
   * 
   * @param eventsRepository
   * @param repertoiresService
   * @param invitationsRepository
   * @param songsService
   * @param artistsService
   * @param musicalGenresRepository
   * @param usersRolesRepository
   * @param usersMusicalBandsRepository
   * @param musicalRolesUsersRepository
   * @param musicalRolesRepository
   * @param rolesService
   * @param musicalBandsService
   */
  public MusicalBandDeletionServiceImpl(EventsRepository eventsRepository,
      RepertoiresService repertoiresService,
      InvitationsRepository invitationsRepository,
      SongsService songsService,
      ArtistsService artistsService,
      MusicalGenresRepository musicalGenresRepository,
      UsersRolesRepository usersRolesRepository,
      UsersMusicalBandsRepository usersMusicalBandsRepository,
      MusicalRolesUsersRepository musicalRolesUsersRepository,
      MusicalRolesRepository musicalRolesRepository,
      RolesService rolesService,
      MusicalBandsService musicalBandsService) {
    this.eventsRepository = eventsRepository;
    this.repertoiresService = repertoiresService;
    this.invitationsRepository = invitationsRepository;
    this.songsService = songsService;
    this.artistsService = artistsService;
    this.musicalGenresRepository = musicalGenresRepository;
    this.usersRolesRepository = usersRolesRepository;
    this.usersMusicalBandsRepository = usersMusicalBandsRepository;
    this.musicalRolesUsersRepository = musicalRolesUsersRepository;
    this.musicalRolesRepository = musicalRolesRepository;
    this.rolesService = rolesService;
    this.musicalBandsService = musicalBandsService;
  }

  @Transactional
  @Override
  public void deleteMusuicalBand(UUID musicalBandId) {
    log.info("Proceding to delete the musical band: {}", musicalBandId);

    log.info("Deleting events related to musical band: {}", musicalBandId);
    eventsRepository.deleteByMusicalBandId(musicalBandId);

    repertoiresService.deleteByMusicalBandId(musicalBandId);

    log.info("Deleting invitations related to musical band: {}", musicalBandId);
    invitationsRepository.deleteByMusicalBandId(musicalBandId);

    songsService.deleteSongsByMusicalBandId(musicalBandId);

    artistsService.deleteArtistsByMusicalBandId(musicalBandId);

    log.info("Deleting musical genres related to musical band: {}", musicalBandId);
    musicalGenresRepository.deleteByMusicalBandId(musicalBandId);

    log.info("Deleting users roles related to musical band: {}", musicalBandId);
    usersRolesRepository.deleteByMusicalBandId(musicalBandId);

    log.info("Deleting users related to musical band: {}", musicalBandId);
    usersMusicalBandsRepository.deleteByMusicalBandId(musicalBandId);

    log.info("Deleting musical roles user related to musical band: {}", musicalBandId);
    musicalRolesUsersRepository.deleteByMusicalBandId(musicalBandId);

    log.info("Deleting musical roles related to musical band: {}", musicalBandId);
    musicalRolesRepository.deleteByMusicalBandId(musicalBandId);

    rolesService.deleteRoleAndRolesPermissionsByMusicalBandId(musicalBandId);

    musicalBandsService.deleteById(musicalBandId);
  }
}
