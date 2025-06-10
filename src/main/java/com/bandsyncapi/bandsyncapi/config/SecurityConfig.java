package com.bandsyncapi.bandsyncapi.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.bandsyncapi.bandsyncapi.api.v1.filter.JWTAuthorizationFilter;
import com.bandsyncapi.bandsyncapi.api.v1.services.CustomUserDetailsService;

/**
 * Security configuration class for the application.
 * This class configures the security settings for the application.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final CustomUserDetailsService customUserDetailsService;

  private final JWTAuthorizationFilter jwtAuthorizationFilter;

  /**
   * Constructor for SecurityConfig.
   *
   * @param customUserDetailsService the CustomUserDetailsService to use for
   *                                 authentication
   * @param jwtAuthorizationFilter   the JWTAuthorizationFilter to use for
   *                                 authorization
   */
  public SecurityConfig(CustomUserDetailsService customUserDetailsService,
      JWTAuthorizationFilter jwtAuthorizationFilter) {
    this.customUserDetailsService = customUserDetailsService;
    this.jwtAuthorizationFilter = jwtAuthorizationFilter;
  }

  /**
   * Configures the security filter chain for the application.
   *
   * @param http the HttpSecurity object to configure
   * @return the configured SecurityFilterChain
   * @throws Exception if an error occurs during configuration
   */
  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    return http
        .csrf(AbstractHttpConfigurer::disable)
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .authorizeHttpRequests(request -> request
            .requestMatchers("/api/v1/users/auth/**", "/api/v1/users/register").permitAll()
            .anyRequest().authenticated())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Arrays.asList("*"));
    configuration.setAllowedMethods(Arrays.asList("POST", "PUT", "GET", "OPTIONS", "DELETE", "PATCH", "OPTIONS"));
    configuration.setAllowedHeaders(Arrays.asList("*"));
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  /**
   * Configures the authentication provider for the application.
   *
   * @return the configured AuthenticationProvider
   */
  @Bean
  AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
    authenticationProvider.setUserDetailsService(customUserDetailsService);
    authenticationProvider.setPasswordEncoder(passwordEncoder());
    return authenticationProvider;
  }

  /**
   * Configures the authentication manager for the application.
   *
   * @param authenticationConfiguration the AuthenticationConfiguration object to
   *                                    use
   * @return the configured AuthenticationManager
   * @throws Exception if an error occurs during configuration
   */
  @Bean
  AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
      throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  /**
   * Configures the password encoder for the application.
   *
   * @return the configured PasswordEncoder
   */
  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
