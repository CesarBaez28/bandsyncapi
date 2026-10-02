package com.bandsyncapi.bandsyncapi.api.v1.services.impl;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verifyNoInteractions;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bandsyncapi.bandsyncapi.api.v1.mappers.SetListDetailsMapper;
import com.bandsyncapi.bandsyncapi.api.v1.mappers.SetListsMapper;
import com.bandsyncapi.bandsyncapi.api.v1.mappers.SetMapper;
import com.bandsyncapi.bandsyncapi.api.v1.models.SetListsModel;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.SetListSongsRepository;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.SetListsRepository;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.SetsRepository;

@ExtendWith(MockitoExtension.class)
class SetListsServiceImplTest {

  @Mock
  private SetListsRepository setListsRepository;

  @Mock
  private SetListsMapper setListsMapper;

  @Mock
  private SetMapper setMapper;

  @Mock
  private SetListDetailsMapper setListDetailsMapper;

  @Mock
  private SetsRepository setsRepository;

  @Mock
  private SetListSongsRepository setListSongsRepository;

  @InjectMocks
  private SetListsServiceImpl setListsService;

  @Test
  void deletesSongsSetsAndSetListsInForeignKeyOrder() {
    UUID musicalBandId = UUID.randomUUID();
    UUID setListId = UUID.randomUUID();
    UUID setId = UUID.randomUUID();
    Set<UUID> setListIds = Set.of(setListId);
    Set<UUID> setIds = Set.of(setId);

    given(setListsRepository.findByMusicalBandId(musicalBandId))
        .willReturn(List.of(new SetListsModel(setListId)));
    
    given(setsRepository.findIdsBySetListIds(setListIds)).willReturn(List.of(setId));

    setListsService.deleteSetListsByMusicalBandId(musicalBandId);

    InOrder deletionOrder = inOrder(setListSongsRepository, setsRepository, setListsRepository);
    
    deletionOrder.verify(setListSongsRepository).deleteBySetIds(setIds);
    deletionOrder.verify(setsRepository).deleteAllByIdInBatch(setIds);
    deletionOrder.verify(setListsRepository).deleteAllByIdInBatch(setListIds);
  }

  @Test
  void skipsSetAndSongQueriesWhenBandHasNoSetLists() {
    UUID musicalBandId = UUID.randomUUID();
   
    given(setListsRepository.findByMusicalBandId(musicalBandId)).willReturn(List.of());

    setListsService.deleteSetListsByMusicalBandId(musicalBandId);

    verifyNoInteractions(setsRepository, setListSongsRepository);
  }
}