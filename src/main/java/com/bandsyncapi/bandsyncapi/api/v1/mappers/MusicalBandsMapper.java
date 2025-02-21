package com.bandsyncapi.bandsyncapi.api.v1.mappers;

import org.mapstruct.Mapper;

import com.bandsyncapi.bandsyncapi.api.v1.dto.musicalbands.MusicalBandsDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.musicalbands.MusicalBandsPostDto;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalBandsModel;

@Mapper(componentModel = "spring")
public interface MusicalBandsMapper {
  
  MusicalBandsDto toDto(MusicalBandsModel musicalBandsModel);

  MusicalBandsModel toModel(MusicalBandsPostDto musicalBandsPostDto);
}
