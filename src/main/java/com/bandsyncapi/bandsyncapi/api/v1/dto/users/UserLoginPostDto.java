package com.bandsyncapi.bandsyncapi.api.v1.dto.users;

/**
 * This record represent the request body for the login endpoint.
 */
public record UserLoginPostDto (String username, String password) {
}
