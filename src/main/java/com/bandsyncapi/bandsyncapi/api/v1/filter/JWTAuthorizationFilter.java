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
import com.bandsyncapi.bandsyncapi.api.v1.services.MusicalBandsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * JWTAuthorizationFilter is a filter that checks the JWT token in the request
 * header.
 * 
 * This filter is responsible for validating the JWT token and setting the
 * authentication
 * in the security context.
 */
@Component
public class JWTAuthorizationFilter extends OncePerRequestFilter {

  private final JWTService jwtService;

  private final CustomUserDetailsService customUserDetailsService;

  private final MusicalBandsService musicalBandsService;

  public JWTAuthorizationFilter(JWTService jwtService, CustomUserDetailsService customUserDetailsService,
      MusicalBandsService musicalBandsService) {
    this.jwtService = jwtService;
    this.customUserDetailsService = customUserDetailsService;
    this.musicalBandsService = musicalBandsService;
  }

  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {

    String token = request.getHeader("Authorization");

    // If the token is null or empty, continue the filter chain
    if (token == null || token.isEmpty()) {
      filterChain.doFilter(request, response);
      return;
    }

    token = token.substring(7);
    String username = jwtService.getUsernameFromToken(token);

    // If the username is null or there is no authentication in the security
    // context, continue the filter chain
    if (username == null || SecurityContextHolder.getContext().getAuthentication() != null) {
      filterChain.doFilter(request, response);
      return;
    }

    UUID musicalBandId = getMusicalBandId(request);

    UserDetails userDetails = (musicalBandId != null)
        ? customUserDetailsService.loadUserByUsernameAndMusicalBandId(username, musicalBandId)
        : customUserDetailsService.loadUserByUsername(username);

    // if the token is not valid, continue the filter chain
    if (userDetails == null || !jwtService.isTokenValid(token, userDetails.getUsername())) {
      filterChain.doFilter(request, response);
      return;
    }

    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null,
        userDetails.getAuthorities());

    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    filterChain.doFilter(request, response);
  }

  /**
   * Gets the musical band ID from the request URI.
   * 
   * If a request contains a musical band ID, it means that the user is making a
   * request
   * from a specific musical band.
   * 
   * So, we get the musical band ID in oder to get the roles and permissions of
   * the user in
   * that specific musical band using the loadUserByUsernameAndMusicalBandId
   * method.
   * 
   * @param request the HttpServletRequest object
   * @return
   */
  private UUID getMusicalBandId(HttpServletRequest request) {
    String requestURI = request.getRequestURI();
    String[] pathSegments = requestURI.split("/");

    // We start at index 3 because the first two segments are "api" and "v(version
    // number)"
    for (int i = 3; i < pathSegments.length; i++) {
      if (isValidUUID(pathSegments[i]) && musicalBandsService.existsById(UUID.fromString(pathSegments[i]))) {
        return UUID.fromString(pathSegments[i]);
      }
    }

    return null;
  }

  /**
   * Checks if the given string is a valid UUID.
   * 
   * @param uuid the string to check
   * @return true if the string is a valid UUID, false otherwise
   */
  private boolean isValidUUID(String uuid) {
    try {
      UUID.fromString(uuid);
      return true;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }

}
