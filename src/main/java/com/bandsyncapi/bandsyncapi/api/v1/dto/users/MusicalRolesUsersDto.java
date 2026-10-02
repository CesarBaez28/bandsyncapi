package com.bandsyncapi.bandsyncapi.api.v1.dto.users;

import java.util.List;
import java.util.UUID;

import com.bandsyncapi.bandsyncapi.api.v1.dto.musicalroles.MusicalRolesDto;

/**
 * Dto for MusicalRolesUsersModel
 */
public record MusicalRolesUsersDto(UUID userId, List<MusicalRolesDto> musicalRoles) {

}
