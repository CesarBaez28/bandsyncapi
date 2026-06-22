package com.bandsyncapi.bandsyncapi.api.v1.dto.roles;

import java.util.UUID;

/**
 * Dto to transfer admin role when a user is going to delete his account and he
 * is the only admin in a band,
 */
public record TransferAdminRoleDto(
    UUID musicalBandId,
    UUID userId,
    Integer adminId) {
}