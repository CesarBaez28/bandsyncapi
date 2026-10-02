package com.bandsyncapi.bandsyncapi.api.v1.mappers;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.mapstruct.BeanMapping;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.bandsyncapi.bandsyncapi.api.v1.dto.setlists.SetDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.setlists.SetListSongsDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.setlists.SetListsDto;
import com.bandsyncapi.bandsyncapi.api.v1.models.SetListSongsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.SetsModel;

/**
 * Maps the set list details model data to its response DTOs.
 */
@Mapper(componentModel = "spring")
public interface SetListDetailsMapper {

  default List<SetListSongsDto> toDtoList(
      List<SetsModel> sets,
      Map<UUID, List<SetListSongsModel>> songsBySetId,
      SetListsDto setListDto) {
    return sets.stream()
        .map(set -> toDto(set, setListDto, songsBySetId.getOrDefault(set.getId(), List.of())))
        .toList();
  }

  @Mapping(target = "set", expression = "java(toSetDto(set, setListDto))")
  @Mapping(target = "songs", source = "setListSongs")
  SetListSongsDto toDto(SetsModel set, SetListsDto setListDto, List<SetListSongsModel> setListSongs);

  @BeanMapping(builder = @Builder(disableBuilder = true))
  @Mapping(target = "id", source = "set.id")
  @Mapping(target = "setList", source = "setListDto")
  @Mapping(target = "name", source = "set.name")
  @Mapping(target = "orderIndex", source = "set.orderIndex")
  @Mapping(target = "status", source = "set.status")
  SetDto toSetDto(SetsModel set, SetListsDto setListDto);
}
