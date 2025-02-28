package com.bandsyncapi.bandsyncapi.api.v1.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.bandsyncapi.bandsyncapi.api.v1.dto.users.UsersDto;
import com.bandsyncapi.bandsyncapi.api.v1.models.UsersModel;

@Mapper(componentModel = "spring")
public interface UsersMapper {

  UsersDto toDto (UsersModel usersModel);

  List<UsersDto> toDtoList (List<UsersModel> usersModelList);
}
