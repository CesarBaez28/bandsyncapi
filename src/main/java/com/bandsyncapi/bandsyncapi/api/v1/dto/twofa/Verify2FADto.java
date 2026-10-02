package com.bandsyncapi.bandsyncapi.api.v1.dto.twofa;

/**
 * Dto for verifying 2FA code
 */
public record Verify2FADto(String secret,Integer code) {
}
