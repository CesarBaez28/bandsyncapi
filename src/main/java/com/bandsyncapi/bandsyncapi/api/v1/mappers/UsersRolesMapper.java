package com.bandsyncapi.bandsyncapi.api.v1.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.bandsyncapi.bandsyncapi.api.v1.dto.roles.UserRoleDto;
import com.bandsyncapi.bandsyncapi.api.v1.models.UsersRolesModel;

@Mapper(componentModel = "spring")
public interface UsersRolesMapper {

  List<UserRoleDto> toDtoList (List<UsersRolesModel> usersRolesModel);
}
