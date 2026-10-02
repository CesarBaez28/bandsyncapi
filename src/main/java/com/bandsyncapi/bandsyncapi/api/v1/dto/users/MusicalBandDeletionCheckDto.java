package com.bandsyncapi.bandsyncapi.api.v1.dto.users;

import java.util.List;

import com.bandsyncapi.bandsyncapi.api.v1.dto.musicalbands.MusicalBandsDto;

import lombok.Builder;

/**
 * Dto to check if a musical band can be deleted, it contains the musical band
 * id, admin id, name and members
 */
@Builder
public record MusicalBandDeletionCheckDto(
    MusicalBandsDto musicalBand,
    Integer adminId,
    List<UsersDto> members) {
}