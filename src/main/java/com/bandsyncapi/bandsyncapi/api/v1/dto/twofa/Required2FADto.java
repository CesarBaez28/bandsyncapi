package com.bandsyncapi.bandsyncapi.api.v1.dto.twofa;

import lombok.Builder;

/**
 * Represent the response when a user needs two factor authentication
 */
@Builder
public record Required2FADto(String status, String tempToken) {
}
