package com.bandsyncapi.bandsyncapi.api.v1.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.bandsyncapi.bandsyncapi.api.v1.dto.users.UserRegisterPostDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.users.UserSessionDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.users.UsersDto;
import com.bandsyncapi.bandsyncapi.api.v1.models.UsersModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.UsersStatusModel;

@Mapper(componentModel = "spring")
public interface UsersMapper {

  UsersDto toDto (UsersModel usersModel);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "firstName", constant = "")
  @Mapping(target = "lastName", constant = "")
  @Mapping(target = "phone", constant = "")
  @Mapping(target = "photo", constant = "")  
  @Mapping(target = "status", constant = "true")
  UsersModel toModelFromRegisterDto(UserRegisterPostDto userRegisterPostDto, UsersStatusModel userStatus);

  List<UsersDto> toDtoList (List<UsersModel> usersModelList);

  UserSessionDto toSessionDto(UsersModel usersModel, String accessToken);
}
