package com.bandsyncapi.bandsyncapi.api.v1.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.bandsyncapi.bandsyncapi.api.v1.dto.permissions.TypeOfPermissionDto;
import com.bandsyncapi.bandsyncapi.api.v1.models.TypeOfPermissionsModel;

/**
 * Mapper interface for TypeOfPermissions
 */
@Mapper(componentModel = "spring")
public interface TypeOfPermissionsMapper {

  List<TypeOfPermissionDto> toDtoList (List<TypeOfPermissionsModel> typeOfPermissionsModelList);
}
