package com.bandsyncapi.bandsyncapi.api.v1.services;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

/**
 * Service class for handling JWT operations.
 * 
 * This class is responsible for generating and validating JWT tokens.
 */
@Service
public class JWTService {

  @Value("${secret.key.jwt}")
  private String secretKey;

  /**
   * Generates a JWT token for the given username.
   *
   * @param username the username for which to generate the token
   * @return the generated JWT token
   */
  public String generateToken(String username) {

    Map<String, Object> claims = new HashMap<>();

    claims.put("type", "ACCESS");

    return Jwts.builder()
        .claims()
        .add(claims)
        .subject(username)
        .issuedAt(new java.util.Date(System.currentTimeMillis()))
        .expiration(new java.util.Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours
        .and()
        .signWith(getKey())
        .compact();
  }

  /**
   * Generates a temp JWT token for the given username.
   * 
   * @param username - username
   * @return the generated temp JWT token
   */
  public String generateTempToken(String username) {
    Map<String, Object> claims = new HashMap<>();

    claims.put("type", "TEMP_2FA");

    return Jwts.builder()
        .claims()
        .add(claims)
        .subject(username)
        .issuedAt(new java.util.Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + 5 * 60 * 1000)) // 5 min
        .and()
        .signWith(getKey())
        .compact();
  }

  /**
   * Generates a JWT token for a band invitation.
   *
   * @param claims    the claims to include in the token (e.g., bandId, email,
   *                  invitationId)
   * @param expiresAt the expiration date of the token
   * @return the generated JWT token
   */
  public String generateInvitationToken(Map<String, Object> claims, LocalDateTime expiresAt) {
    return Jwts.builder()
        .claims()
        .add(claims)
        .issuedAt(new java.util.Date(System.currentTimeMillis()))
        .expiration(java.sql.Timestamp.valueOf(expiresAt))
        .and()
        .signWith(getKey())
        .compact();
  }

  /**
   * Gets the secret key used for signing the JWT tokens.
   * 
   * @return the secret key
   */
  public SecretKey getKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  /**
   * Gets the username from the given JWT token.
   *
   * @param token the JWT token
   * @return the username extracted from the token
   */
  public String getUsernameFromToken(String token) {
    return getClaim(token, Claims::getSubject);
  }

  /**
   * Gets the claim from the given JWT token.
   *
   * @param token          the JWT token
   * @param claimsFunction the function to extract the claim
   * @param <T>            the type of the claim
   * @return the extracted claim
   */
  public <T> T getClaim(String token, Function<Claims, T> claimsFunction) {
    Claims claims = extractAllClaims(token);
    return claimsFunction.apply(claims);
  }

  /**
   * Extracts all claims from the given JWT token.
   *
   * @param token the JWT token
   * @return the claims extracted from the token
   */
  public Claims extractAllClaims(String token) {
    return Jwts.parser()
        .verifyWith(getKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  /**
   * Validates the given JWT token.
   *
   * @param token    the JWT token
   * @param username the username to validate against
   * @return true if the token is valid, false otherwise
   */
  public boolean isTokenValid(String token, String username) {
    final String extractedUsername = getUsernameFromToken(token);
    return (extractedUsername.equals(username) && !isTokenExpired(token));
  }

  /**
   * Checks if the given JWT token is expired.
   *
   * @param token the JWT token
   * @return true if the token is expired, false otherwise
   */
  private boolean isTokenExpired(String token) {
    return getExpirationDateFromToken(token).before(new Date());
  }

  /**
   * Gets the expiration date from the given JWT token.
   *
   * @param token the JWT token
   * @return the expiration date extracted from the token
   */
  private Date getExpirationDateFromToken(String token) {
    return getClaim(token, Claims::getExpiration);
  }
}
