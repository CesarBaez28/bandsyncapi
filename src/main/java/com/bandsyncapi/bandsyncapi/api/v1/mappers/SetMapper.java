package com.bandsyncapi.bandsyncapi.api.v1.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.bandsyncapi.bandsyncapi.api.v1.dto.setlists.UpdateSetDto;
import com.bandsyncapi.bandsyncapi.api.v1.models.SetsModel;

/**
 * 
 * Mapper for SetsModel
 */
@Mapper(componentModel = "spring")
public interface SetMapper {

  @Mapping (target = "status", ignore = true)
  @Mapping (target = "list", ignore = true )
  SetsModel toModelFromUpdateSetDto(UpdateSetDto updateSetDto);
}
