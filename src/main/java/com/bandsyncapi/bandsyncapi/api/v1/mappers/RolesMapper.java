package com.bandsyncapi.bandsyncapi.api.v1.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.bandsyncapi.bandsyncapi.api.v1.dto.roles.RolesDto;
import com.bandsyncapi.bandsyncapi.api.v1.models.RolesModel;

@Mapper(componentModel = "spring")
public interface RolesMapper {

  List<RolesDto> toDtoList (List<RolesModel> rolesModelList);
}
