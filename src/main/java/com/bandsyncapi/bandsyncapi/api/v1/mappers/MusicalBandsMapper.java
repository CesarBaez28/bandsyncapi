package com.bandsyncapi.bandsyncapi.api.v1.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.bandsyncapi.bandsyncapi.api.v1.dto.musicalbands.MusicalBandsDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.musicalbands.MusicalBandsPostDto;
import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalBandsModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.UsersMusicalBandsModel;

@Mapper(componentModel = "spring")
public interface MusicalBandsMapper {
  
  MusicalBandsDto toDto(MusicalBandsModel musicalBandsModel);

  @Mapping(target = "hyphenatedName", ignore = true)
  MusicalBandsModel toModel(MusicalBandsPostDto musicalBandsPostDto);

  @Mapping(source = "musicalBand.id", target = "id")
  @Mapping(source = "musicalBand.name", target = "name")
  @Mapping(source = "musicalBand.hyphenatedName", target = "hyphenatedName")
  @Mapping(source = "musicalBand.logo", target = "logo")
  @Mapping(source = "musicalBand.address", target = "address")
  @Mapping(source = "musicalBand.phone", target = "phone")
  @Mapping(source = "musicalBand.email", target = "email")
  @Mapping(source = "musicalBand.status", target = "status")
  MusicalBandsDto toDto(UsersMusicalBandsModel model);

  List<MusicalBandsDto> toDtoListFromUsersMusicalBand (List<UsersMusicalBandsModel> usersMusicalBandsModel);
}
