package com.bandsyncapi.bandsyncapi.api.v1.mappers;

import java.util.UUID;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.bandsyncapi.bandsyncapi.api.v1.dto.musicalgenres.MusicalGenreDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.musicalgenres.MusicalGenrePostDto;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalBandsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalGenresModel;

/**
 * Mapper for the MusicalGenresModel
 */
@Mapper(componentModel = "spring")
public interface MusicalGenresMapper {

  MusicalGenreDto toDto(MusicalGenresModel musicalGenresModel);
  
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "musicalBand", source = "musicalBandId", qualifiedByName = "mapBandIdToEntity")
  @Mapping(target = "status", constant = "true")
  MusicalGenresModel toModel(MusicalGenrePostDto dto);

  @Named("mapBandIdToEntity")
  default MusicalBandsModel mapBandIdToEntity(UUID bandId) {
    if (bandId == null) {
      return null;
    }
    return new MusicalBandsModel(bandId); 
  }
}
