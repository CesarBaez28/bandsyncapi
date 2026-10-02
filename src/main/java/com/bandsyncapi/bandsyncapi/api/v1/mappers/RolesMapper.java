package com.bandsyncapi.bandsyncapi.api.v1.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.bandsyncapi.bandsyncapi.api.v1.dto.roles.RolesDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.roles.RolesPermissionsDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.roles.RolesPostDto;
import com.bandsyncapi.bandsyncapi.api.v1.models.RolesModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.RolesPermissionsModel;

@Mapper(componentModel = "spring")
public interface RolesMapper {

  @Mapping(target = "id", ignore = true)
  RolesModel toModel (RolesPostDto rolesPostDto);

  @Mapping(target = "musicalBand", ignore = true)
  RolesModel toModel(RolesDto rolesDto);

  RolesDto toDto (RolesModel rolesModel);

  RolesPermissionsDto toRolesPermissionsDto (RolesDto role, List<RolesPermissionsModel> permissions);

  List<RolesDto> toDtoList (List<RolesModel> rolesModelList);
}
