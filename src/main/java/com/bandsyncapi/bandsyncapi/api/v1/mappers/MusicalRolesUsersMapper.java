package com.bandsyncapi.bandsyncapi.api.v1.mappers;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;

import com.bandsyncapi.bandsyncapi.api.v1.dto.musicalroles.MusicalRolesDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.users.MusicalRolesUsersDto;
import com.bandsyncapi.bandsyncapi.api.v1.projections.MusicalRolesUsersProjection;

/**
 * Mapper for MusicalRolesUsersModel
 */
@Mapper(componentModel = "spring")
public interface MusicalRolesUsersMapper {

  MusicalRolesDto toMusicalRolesDto(MusicalRolesUsersProjection musicalRolesUsersProjection);

  /**
   * Returns a MusicalRolesUsersDto List group by user id
   * 
   * @param projections
   * @return -  A MusicalRolesUsersDto List
   */
  default List<MusicalRolesUsersDto> toDtoList(List<MusicalRolesUsersProjection> projections) {
    Map<UUID, List<MusicalRolesDto>> group = projections.stream()
        .collect(Collectors.groupingBy(
            MusicalRolesUsersProjection::getUserId,
            Collectors.mapping(this::toMusicalRolesDto, Collectors.toList())));

    return group.entrySet().stream()
        .map(entry -> new MusicalRolesUsersDto(entry.getKey(), entry.getValue())).toList();
  }
}
