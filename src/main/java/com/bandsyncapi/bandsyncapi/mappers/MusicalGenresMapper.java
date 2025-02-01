package com.bandsyncapi.bandsyncapi.mappers;

import java.util.UUID;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.bandsyncapi.bandsyncapi.dto.musicalgenres.MusicalGenreDto;
import com.bandsyncapi.bandsyncapi.dto.musicalgenres.MusicalGenrePostDto;
import com.bandsyncapi.bandsyncapi.models.MusicalBandsModel;
import com.bandsyncapi.bandsyncapi.models.MusicalGenresModel;

/**
 * Mapper for the MusicalGenresModel
 */
@Mapper(componentModel = "spring")
public interface MusicalGenresMapper {

  MusicalGenreDto toDto(MusicalGenresModel musicalGenresModel);
  
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "musicalBand", source = "musicalBandId", qualifiedByName = "mapBandIdToEntity")
  @Mapping(target = "status", constant = "true")
  MusicalGenresModel toEntity(MusicalGenrePostDto dto);

  @Named("mapBandIdToEntity")
  default MusicalBandsModel mapBandIdToEntity(UUID bandId) {
    if (bandId == null) {
      return null;
    }
    return new MusicalBandsModel(bandId); 
  }
}
