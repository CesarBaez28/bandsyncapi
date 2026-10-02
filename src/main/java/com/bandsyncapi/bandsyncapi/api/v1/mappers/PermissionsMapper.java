package com.bandsyncapi.bandsyncapi.api.v1.mappers;

import org.mapstruct.Mapper;

import com.bandsyncapi.bandsyncapi.api.v1.dto.permissions.PermissionDto;
import com.bandsyncapi.bandsyncapi.api.v1.models.PermissionsModel;

/**
 * Mapper interface for Permissions
 */
@Mapper(componentModel = "spring")
public interface PermissionsMapper {

  PermissionDto toDto (PermissionsModel permissionsModel);
}
