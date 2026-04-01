package com.bandsyncapi.bandsyncapi.api.v1.dto.users;

/**
 * DTO for forgot password request
 * 
 * @param email - The email of the user requesting a password reset
 */
public record ForgotPasswordRequestDto(String email) {
}
