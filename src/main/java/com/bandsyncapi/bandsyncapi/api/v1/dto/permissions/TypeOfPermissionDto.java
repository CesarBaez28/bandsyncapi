package com.bandsyncapi.bandsyncapi.api.v1.dto.permissions;

/**
 * Data Transfer Object for TypeOfPermission
 */
public record TypeOfPermissionDto(
    Integer id,
    String name,
    Boolean status) 
{}
