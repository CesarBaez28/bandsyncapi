package com.bandsyncapi.bandsyncapi.api.v1.filter;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.bandsyncapi.bandsyncapi.api.v1.models.MusicalBandsModel;
import com.bandsyncapi.bandsyncapi.api.v1.services.CustomUserDetailsService;
import com.bandsyncapi.bandsyncapi.api.v1.services.JWTService;
import com.bandsyncapi.bandsyncapi.api.v1.services.MusicalBandsService;

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

    log.info("Getting token from Authorization header...");

    String token = request.getHeader("Authorization");

    // If the token is null or empty, continue the filter chain
    if (token == null || token.isEmpty()) {
      log.info("Token not found in Authorization header");
      filterChain.doFilter(request, response);
      return;
    }

    token = token.substring(7);

    log.info("Token obtained successfully");

    String username = jwtService.getUsernameFromToken(token);

    log.info("Getting username from token");

    // If the username is null or there is no authentication in the security
    // context, continue the filter chain
    if (username == null || SecurityContextHolder.getContext().getAuthentication() != null) {
      log.info("Username is null");
      filterChain.doFilter(request, response);
      return;
    }

    UUID musicalBandId = getMusicalBandId(request);

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

  /**
   * Gets the musical band id from the request URI.
   * 
   * If a request contains a musical band name, it means that the user is making a
   * request
   * from a specific musical band.
   * 
   * So, we get the musical band ID from the musical band name in oder to get the roles and permissions of
   * the user in
   * that specific musical band using the loadUserByUsernameAndMusicalBandId
   * method.
   * 
   * @param request the HttpServletRequest object
   * @return musical band id
   */
  private UUID getMusicalBandId(HttpServletRequest request) {
    String requestURI = request.getRequestURI();
    String[] pathSegments = requestURI.split("/");
    
    // At index 3 because the first two segments are "api" and "v1"
    String musicalBandName = pathSegments[3];
    Optional<MusicalBandsModel> musicalBand = musicalBandsService.findByHyphenatedName(musicalBandName);

    if (musicalBand.isPresent()) {
      log.info("Musical band found successfully: {}" + musicalBand.get());
      return musicalBand.get().getId();
    }

    log.info("Musical band not found in URI");

    return null;
  }
}
