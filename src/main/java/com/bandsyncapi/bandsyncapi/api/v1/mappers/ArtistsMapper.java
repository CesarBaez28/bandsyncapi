package com.bandsyncapi.bandsyncapi.api.v1.mappers;

import java.util.List;
import java.util.UUID;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.bandsyncapi.bandsyncapi.api.v1.dto.artists.ArtistsDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.artists.ArtistsPostDto;
import com.bandsyncapi.bandsyncapi.api.v1.models.ArtistsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalBandsModel;

@Mapper(componentModel = "spring")
public interface ArtistsMapper {

  ArtistsDto toDto(ArtistsModel musicalGenresModel);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "musicalBand", source = "musicalBandId", qualifiedByName = "mapBandIdToEntity")
  @Mapping(target = "status", constant = "true")
  ArtistsModel toModel(ArtistsPostDto dto);

  List<ArtistsDto> toDtoList(List<ArtistsModel> artistsModelList);

  @Named("mapBandIdToEntity")
  default MusicalBandsModel mapBandIdToEntity(UUID bandId) {
    if (bandId == null) {
      return null;
    }
    return new MusicalBandsModel(bandId);
  }
}
