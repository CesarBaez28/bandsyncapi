package com.bandsyncapi.bandsyncapi.api.v1.dto.users;

import java.util.UUID;

/**
 * DTO for reset password request
 * 
 * @param token       - The password reset token
 * @param newPassword - The new password to set for the user
 */
public record ResetPasswordRequestDto(UUID token, String newPassword) {
}
