package com.bandsyncapi.bandsyncapi.api.v1.dto.permissions;

/**
 * Data Transfer Object for Permission
 */
public record PermissionDto(
    Integer id,
    TypeOfPermissionDto typeOfPermission,
    String name,
    Boolean status
) {}
