package com.bandsyncapi.bandsyncapi.api.v1.mappers;

import java.util.List;
import java.util.UUID;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.bandsyncapi.bandsyncapi.api.v1.dto.musicalroles.MusicalRolesDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.musicalroles.MusicalRolesPostDto;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalBandsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalRolesModel;

/**
 * Mapper for the MusicalRolesModel
 */
@Mapper(componentModel = "spring")
public interface MusicalRolesMapper {

  MusicalRolesDto toDto(MusicalRolesModel musicalRolesModel);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "musicalBand", source = "musicalBandId", qualifiedByName = "mapBandIdToEntity")
  @Mapping(target = "status", constant = "true")
  MusicalRolesModel toModel(MusicalRolesPostDto musicalRolesPostDto);

  List<MusicalRolesDto> toDtoList (List<MusicalRolesModel> musicalRolesModels);

  @Named("mapBandIdToEntity")
  default MusicalBandsModel mapBandIdToEntity(UUID bandId) {
    if (bandId == null) {
      return null;
    }
    return new MusicalBandsModel(bandId);
  }
}
