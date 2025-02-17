package com.bandsyncapi.bandsyncapi.api.v1.mappers;

import java.util.List;
import java.util.UUID;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.bandsyncapi.bandsyncapi.api.v1.dto.repertoires.RepertoiresDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.repertoires.RepertoiresPostDto;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalBandsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.RepertoiresModel;

/**
 * Mapper for the MusicalRolesModel
 */
@Mapper(componentModel = "spring")
public interface RepertoiresMapper {

  RepertoiresDto toDto (RepertoiresModel repertoiresModel);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "musicalBand", source = "musicalBand", qualifiedByName = "mapBandIdToEntity")
  RepertoiresModel toModel (RepertoiresPostDto repertoiresPostDto);

  List<RepertoiresDto> toDtoList (List<RepertoiresModel> repertoiresModels);

  @Named("mapBandIdToEntity")
  default MusicalBandsModel mapBandIdToEntity(UUID bandId) {
    if (bandId == null) {
      return null;
    }
    return new MusicalBandsModel(bandId);
  }
}
