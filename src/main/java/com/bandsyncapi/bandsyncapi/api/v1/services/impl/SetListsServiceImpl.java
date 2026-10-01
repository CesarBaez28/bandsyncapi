package com.bandsyncapi.bandsyncapi.api.v1.services.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bandsyncapi.bandsyncapi.api.v1.dto.setlists.CreateSetDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.setlists.CreateSetListDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.setlists.SetListDetailsDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.setlists.SetListSongsDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.setlists.SetListsDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.setlists.SetSongsRequestDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.setlists.UpdateSetDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.setlists.UpdateSetListDto;
import com.bandsyncapi.bandsyncapi.api.v1.mappers.SetListDetailsMapper;
import com.bandsyncapi.bandsyncapi.api.v1.mappers.SetListsMapper;
import com.bandsyncapi.bandsyncapi.api.v1.mappers.SetMapper;
import com.bandsyncapi.bandsyncapi.api.v1.models.SetListSongsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.SetListsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.SetsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.SongsModel;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.SetListSongsRepository;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.SetListsRepository;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.SetsRepository;
import com.bandsyncapi.bandsyncapi.api.v1.services.SetListsService;

import lombok.extern.slf4j.Slf4j;

/**
 * This class implements the SetListsService interface and provides methods for
 * managing set lists in the application.
 */
@Slf4j
@Service
public class SetListsServiceImpl implements SetListsService {

  private final SetListsRepository setListsRepository;

  private final SetListsMapper setListsMapper;

  private final SetMapper setMapper;

  private final SetListDetailsMapper setListDetailsMapper;

  private final SetsRepository setsRepository;

  private final SetListSongsRepository setListSongsRepository;

  /**
   * Constructor for SetListsServiceImpl.
   *
   * @param setListsRepository     the repository for managing set lists
   * @param setListsMapper         the mapper for converting between DTOs and
   *                               models
   * @param setMapper              the mapper for corveting between DTOs and
   *                               models
   * @param setsRepository         the repository for managing sets
   * @param setListSongsRepository the repository for managing set list songs
   */
  public SetListsServiceImpl(SetListsRepository setListsRepository, SetsRepository setsRepository,
      SetListsMapper setListsMapper, SetMapper setMapper, SetListDetailsMapper setListDetailsMapper,
      SetListSongsRepository setListSongsRepository) {
    this.setListsRepository = setListsRepository;
    this.setListsMapper = setListsMapper;
    this.setMapper = setMapper;
    this.setListDetailsMapper = setListDetailsMapper;
    this.setsRepository = setsRepository;
    this.setListSongsRepository = setListSongsRepository;
  }

  @Override
  public Page<SetListsModel> searchAllByMusicalBandId(UUID musicalBandId, String term, int page, int size) {
    log.info("Finding set lists for musical band with ID: {} and search term: {}", musicalBandId, term);
    return setListsRepository.searchAllByMusicalBandId(musicalBandId, term, PageRequest.of(page, size));
  }

  @Override
  public List<SetListsModel> findByMuscalBandId(UUID musicalBandId) {
    log.info("Finding set lists for musical band with ID: {}", musicalBandId);
    return setListsRepository.findByMusicalBandId(musicalBandId);
  }

  @Override
  public SetListDetailsDto findSetListDetailsById(UUID setListId) {
    log.info("Finding set list details for set list with ID: {}", setListId);

    SetListsModel setListModel = setListsRepository.findById(setListId)
        .orElseThrow(() -> new IllegalArgumentException("Set list not found with ID: " + setListId));

    List<SetsModel> sets = setsRepository.findBySetListIdOrderByOrderIndexAsc(setListId);

    Set<UUID> setIds = sets.stream().map(SetsModel::getId).collect(Collectors.toSet());

    List<SetListSongsModel> setListSongs = setListSongsRepository.findBySetIdsOrderByOrderIndexAsc(setIds);

    SetListsDto setListDto = setListsMapper.toDto(setListModel);

    Map<UUID, List<SetListSongsModel>> songsBySetId = setListSongs.stream()
        .collect(Collectors.groupingBy(setListSong -> setListSong.getSet().getId()));

    List<SetListSongsDto> setListSongsDtos = setListDetailsMapper.toDtoList(sets, songsBySetId, setListDto);

    return SetListDetailsDto.builder()
        .setList(setListDto)
        .sets(setListSongsDtos)
        .build();
  }

  @Override
  @Transactional
  public void saveSetList(CreateSetListDto createSetListDto) {
    log.info("Proceeding to save new Set list with name: {}", createSetListDto.name());

    SetListsModel setListsModel = setListsMapper.toModel(createSetListDto);

    log.info("Saving new set list", setListsModel.getName());

    SetListsModel savedSetLit = setListsRepository.save(setListsModel);

    if (!createSetListDto.sets().isEmpty()) {
      log.info("Proceeding to save sets of the set list", savedSetLit);

      for (CreateSetDto createSetDto : createSetListDto.sets()) {
        var setModel = SetsModel.builder()
            .setList(savedSetLit)
            .name(createSetDto.name())
            .orderIndex(createSetDto.orderIndex())
            .build();

        var savedSet = setsRepository.save(setModel);

        log.info("Set successfully saved with id {}", savedSet.getId());

        List<SetListSongsModel> setListSongsModels = createSetDto.songs().stream()
            .map(setSongDto -> SetListSongsModel.builder()
                .set(savedSet)
                .song(new SongsModel(setSongDto.songId()))
                .orderIndex(setSongDto.orderIndex())
                .notes(setSongDto.notes())
                .build())
            .toList();

        setListSongsRepository.saveAll(setListSongsModels);
        log.info("Songs successfully saved for set with id {}", savedSet.getId());
      }
    }

    log.info("Set list successfully saved with id {}", savedSetLit.getId());
  }

  @Override
  public void deleteSetList(UUID setListId) {
    log.info("deleting set list with id: {}", setListId);

    Set<UUID> setsIds = setsRepository.findBySetListIdOrderByOrderIndexAsc(setListId)
        .stream()
        .map(SetsModel::getId)
        .collect(Collectors.toSet());

    Set<UUID> setSongsIds = setListSongsRepository
        .findBySetIdsOrderByOrderIndexAsc(setsIds)
        .stream()
        .map(SetListSongsModel::getId)
        .collect(Collectors.toSet());

    setListSongsRepository.deleteAllByIdInBatch(setSongsIds);
    setsRepository.deleteAllByIdInBatch(setsIds);
    setListsRepository.deleteById(setListId);
  }

  @Override
  @Transactional
  public void updateSetList(UUID setListId, UpdateSetListDto updateSetListDto) {
    log.info("Updating set list with id: {}", setListId);

    SetListsModel existingSetList = setListsRepository.findById(setListId)
        .orElseThrow(() -> new IllegalArgumentException("Set list not found with ID: " + setListId));

    updateSetListInfo(existingSetList, updateSetListDto);

    List<UpdateSetDto> setsFromRequest = updateSetListDto.sets() == null
        ? List.of()
        : updateSetListDto.sets();

    Set<UUID> existingSetIds = setsRepository
        .findBySetListIdOrderByOrderIndexAsc(setListId)
        .stream()
        .map(SetsModel::getId)
        .collect(Collectors.toSet());

    Set<UUID> processedSetIds = synchronizeSets(setListId, existingSetIds, setsFromRequest);

    deleteRemovedSets(existingSetIds, processedSetIds);

    log.info("Set list updated successfully with id: {}", setListId);
  }

  /**
   * Synchronizes all Sets belonging to the SetList.
   *
   * Returns the IDs of Sets that were present in the request.
   * 
   * Those IDs will be used to delete the sets that existed in the database but
   * weren't
   * included in the update request.
   * 
   * @param setListId
   * @param existingSetIds
   * @param setsFromRequest
   */
  private Set<UUID> synchronizeSets(UUID setListId, Set<UUID> existingSetIds, List<UpdateSetDto> setsFromRequest) {

    Set<UUID> processedSetIds = new HashSet<>();

    List<SetListSongsModel> existingSetListSongs = setListSongsRepository
        .findBySetIdsOrderByOrderIndexAsc(existingSetIds);

    Map<UUID, List<SetListSongsModel>> existingSongsBySet = existingSetListSongs.stream()
        .collect(Collectors.groupingBy(
            song -> song.getSet().getId()));

    for (UpdateSetDto setDto : setsFromRequest) {
      boolean isANewSet = setDto.id() == null;

      if (isANewSet) {
        createSet(setListId, setDto);
        continue;
      }

      updateSet(setListId, existingSongsBySet, setDto);
      processedSetIds.add(setDto.id());
    }

    return processedSetIds;
  }

  /**
   * Updates an existing Set and its songs
   * 
   * @param setListId          - existing set list id
   * @param existingSongsBySet - existing set songs grouping by set id
   * @param setDto             - set dto with the information to update
   */
  private void updateSet(UUID setListId, Map<UUID, List<SetListSongsModel>> existingSongsBySet, UpdateSetDto setDto) {
    SetsModel setToUpdate = setMapper.toModelFromUpdateSetDto(setDto);
    setToUpdate.setSetList(new SetListsModel(setListId));

    SetsModel updatedSet = setsRepository.save(setToUpdate);

    Set<UUID> existingSetSongsIds = existingSongsBySet.getOrDefault(updatedSet.getId(), List.of())
        .stream()
        .map(SetListSongsModel::getId)
        .collect(Collectors.toSet());

    synchronizeSetSongs(updatedSet, setDto, existingSetSongsIds);
  }

  /**
   * Synchronizes the songs belonging to an existing Set.
   * 
   * @param updatedSet          - updadted set
   * @param setDto              - set dto with the information of the songs of the
   *                            set
   * @param existingSetSongsIds - existing set songs id used to determine the
   *                            songs to delete that
   *                            existed before but were not
   *                            included in the new request.
   */
  private void synchronizeSetSongs(SetsModel updatedSet, UpdateSetDto setDto, Set<UUID> existingSetSongsIds) {

    Set<UUID> processedSongIds = new HashSet<>();

    List<SetListSongsModel> setSongsToAdd = new ArrayList<>();

    List<SetListSongsModel> setSongsToUpdate = new ArrayList<>();

    for (SetSongsRequestDto setSong : setDto.songs()) {
      boolean isANewSong = setSong.setSongId() == null;

      if (isANewSong) {
        setSongsToAdd.add(SetListSongsModel.builder()
            .set(updatedSet)
            .song(new SongsModel(setSong.songId()))
            .orderIndex(setSong.orderIndex())
            .notes(setSong.notes())
            .build());
        continue;
      }

      setSongsToUpdate.add(SetListSongsModel.builder()
          .id(setSong.setSongId())
          .set(updatedSet)
          .song(new SongsModel(setSong.songId()))
          .orderIndex(setSong.orderIndex())
          .notes(setSong.notes())
          .build());

      processedSongIds.add(setSong.setSongId());
    }

    setListSongsRepository.saveAll(setSongsToAdd);
    setListSongsRepository.saveAll(setSongsToUpdate);
    deleteRemovedSetSongs(existingSetSongsIds, processedSongIds);
  }

  /**
   * Delete songs that existed before but were not
   * included in the new request.
   * 
   * @param existingSetSongsIds - existing set songs ids
   * @param processedSongIds    - processed song ids
   */
  private void deleteRemovedSetSongs(Set<UUID> existingSetSongsIds, Set<UUID> processedSongIds) {
    Set<UUID> setSongsIdsToDelete = new HashSet<>(existingSetSongsIds);
    setSongsIdsToDelete.removeAll(processedSongIds);

    if (!setSongsIdsToDelete.isEmpty()) {
      setListSongsRepository.deleteAllByIdInBatch(setSongsIdsToDelete);
    }
  }

  /**
   * Deletes Sets that existed in the database but weren't
   * included in the update request.
   * 
   * @param existingSetIds  - existings sets ids
   * @param processedSetIds - processed ids
   */
  private void deleteRemovedSets(Set<UUID> existingSetIds, Set<UUID> processedSetIds) {
    Set<UUID> setsIdsToDelete = new HashSet<>(existingSetIds);
    setsIdsToDelete.removeAll(processedSetIds);

    if (!setsIdsToDelete.isEmpty()) {
      setListSongsRepository.deleteBySetIds(setsIdsToDelete);
      setsRepository.deleteAllByIdInBatch(setsIdsToDelete);
    }
  }

  /**
   * create a set ant its songs
   * 
   * @param set   - set to save
   * @param songs - songs of the set
   */
  private void createSet(UUID setListId, UpdateSetDto setDto) {
    SetsModel setToSave = setMapper.toModelFromUpdateSetDto(setDto);
    setToSave.setSetList(new SetListsModel(setListId));

    SetsModel savedSet = setsRepository.save(setToSave);

    List<SetListSongsModel> setListSongsModels = setDto.songs().stream()
        .map(setSongDto -> SetListSongsModel.builder()
            .set(savedSet)
            .song(new SongsModel(setSongDto.songId()))
            .orderIndex(setSongDto.orderIndex())
            .notes(setSongDto.notes())
            .build())
        .toList();

    setListSongsRepository.saveAll(setListSongsModels);
  }

  /**
   * Save information of the set list
   * 
   * @param existingSetList  - existing set list
   * @param updateSetListDto - new info to update
   */
  private void updateSetListInfo(SetListsModel existingSetList, UpdateSetListDto updateSetListDto) {
    existingSetList.setName(updateSetListDto.name());
    existingSetList.setDescription(updateSetListDto.description());
    existingSetList.setRepertoire(updateSetListDto.repertoire());

    setListsRepository.save(existingSetList);
  }
}
