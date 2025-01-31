package com.bandsyncapi.bandsyncapi.mappers;

import org.mapstruct.Mapper;

import com.bandsyncapi.bandsyncapi.dto.musicalbands.MusicalBandsDto;
import com.bandsyncapi.bandsyncapi.models.MusicalBandsModel;

@Mapper(componentModel = "spring")
public interface MusicalBandsMapper {
  
  MusicalBandsDto toDto(MusicalBandsModel musicalBandsModel);

  MusicalBandsModel toModel(MusicalBandsDto musicalBandsDto);
}
