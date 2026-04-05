package com.bandsyncapi.bandsyncapi.api.v1.dto.twofa;

/**
 * Request to verify login with two factor authentication
 */
public record VerifyLogin2FADto(String tempToken, Integer code) {
}
