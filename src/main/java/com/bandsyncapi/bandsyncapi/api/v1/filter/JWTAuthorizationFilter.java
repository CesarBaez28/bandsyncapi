package com.bandsyncapi.bandsyncapi.api.v1.filter;

import java.io.IOException;
import java.util.UUID;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.bandsyncapi.bandsyncapi.api.v1.services.CustomUserDetailsService;
import com.bandsyncapi.bandsyncapi.api.v1.services.JWTService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * JWTAuthorizationFilter is a filter that checks the JWT token in the request
 * header.
 * 
 * This filter is responsible for validating the JWT token and setting the
 * authentication
 * in the security context.
 */
@Component
@Slf4j
public class JWTAuthorizationFilter extends OncePerRequestFilter {

  private final JWTService jwtService;

  private final CustomUserDetailsService customUserDetailsService;

  public JWTAuthorizationFilter(JWTService jwtService, CustomUserDetailsService customUserDetailsService) {
    this.jwtService = jwtService;
    this.customUserDetailsService = customUserDetailsService;
  }

  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {

    log.info("Getting token from Authorization header...");

    String token = request.getHeader("Authorization");

    // If the token is null or empty, continue the filter chain
    if (token == null || token.isEmpty()) {
      log.info("Token not found in Authorization header");
      filterChain.doFilter(request, response);
      return;
    }

    log.info("Getting Token from header");
    token = token.substring(7);

    log.info("Verifing type of token");
    
    Claims claims = jwtService.extractAllClaims(token);
    String type = claims.get("type", String.class);

    if (!"ACCESS".equals(type)) {
      log.warn("Invalid token type: {}", type);
      filterChain.doFilter(request, response);
      return;
    }

    log.info("Getting username from token");
    String username = jwtService.getUsernameFromToken(token);

    // If the username is null or there is no authentication in the security
    // context, continue the filter chain
    if (username == null || SecurityContextHolder.getContext().getAuthentication() != null) {
      log.info("Username is null");
      filterChain.doFilter(request, response);
      return;
    }

    log.info("Getting musical band id from header");
    String musicalBandHeader = request.getHeader("X-MUSICAL-BAND-ID");
    UUID musicalBandId = null;

    if (musicalBandHeader != null && !musicalBandHeader.isEmpty()) {
      musicalBandId = UUID.fromString(musicalBandHeader);
      log.info("Musical band id obtained: {}", musicalBandId);
    }

    log.info("Getting UserDetails");

    UserDetails userDetails = (musicalBandId != null)
        ? customUserDetailsService.loadUserByUsernameAndMusicalBandId(username, musicalBandId)
        : customUserDetailsService.loadUserByUsername(username);

    // if the token is not valid, continue the filter chain
    if (userDetails == null || !jwtService.isTokenValid(token, userDetails.getUsername())) {
      log.info("Token is invalid or credentials are incorrect");
      filterChain.doFilter(request, response);
      return;
    }

    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null,
        userDetails.getAuthorities());

    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

    log.info("Authorization process finish successfully");

    filterChain.doFilter(request, response);
  }
}
