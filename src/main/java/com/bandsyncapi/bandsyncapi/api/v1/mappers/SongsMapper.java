package com.bandsyncapi.bandsyncapi.api.v1.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.bandsyncapi.bandsyncapi.api.v1.dto.songs.SongsDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.songs.SongsPostDto;
import com.bandsyncapi.bandsyncapi.api.v1.models.SongsModel;

/**
 * Mapper for the SongsModel
 */
@Mapper(componentModel = "spring")
public interface SongsMapper {

  SongsDto toDto (SongsModel songsModel);

  @Mapping(target = "id", ignore = true)
  SongsModel toModel (SongsPostDto songsPostDto);

  List<SongsDto> toDtoList (List<SongsModel> songsModels);
}
