package com.bandsyncapi.bandsyncapi.api.v1.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.bandsyncapi.bandsyncapi.api.v1.dto.setlists.CreateSetListDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.setlists.SetListsDto;
import com.bandsyncapi.bandsyncapi.api.v1.models.SetListsModel;

/**
 * Mapper for the SetListsModel
 */
@Mapper(componentModel = "spring")
public interface SetListsMapper {

  List<SetListsDto> toDtoList(List<SetListsModel> setListsModels);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now(java.time.ZoneId.systemDefault()))")
  @Mapping(target = "status", ignore = true)
  SetListsModel toModel(CreateSetListDto createSetListDto);

  SetListsDto toDto(SetListsModel setListsModel);
}
