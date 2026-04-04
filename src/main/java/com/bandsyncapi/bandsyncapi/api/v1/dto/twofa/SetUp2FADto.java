package com.bandsyncapi.bandsyncapi.api.v1.dto.twofa;

import lombok.Builder;

/**
 * Dto for setting up 2FA
 */
@Builder
public record SetUp2FADto(String secret, String qrUrl) {
}
