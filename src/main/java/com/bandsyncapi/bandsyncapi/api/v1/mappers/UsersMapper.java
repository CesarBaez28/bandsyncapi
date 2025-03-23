package com.bandsyncapi.bandsyncapi.api.v1.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.bandsyncapi.bandsyncapi.api.v1.dto.users.UserRegisterPostDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.users.UsersDto;
import com.bandsyncapi.bandsyncapi.api.v1.models.UsersModel;

@Mapper(componentModel = "spring")
public interface UsersMapper {

  UsersDto toDto (UsersModel usersModel);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "firstName", constant = "")
  @Mapping(target = "lastName", constant = "")
  @Mapping(target = "phone", constant = "")
  @Mapping(target = "photo", constant = "")  
  @Mapping(target = "status", constant = "true")
  UsersModel toModelFromRegisterDto(UserRegisterPostDto userRegisterPostDto);

  List<UsersDto> toDtoList (List<UsersModel> usersModelList);
}
