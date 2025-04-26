package com.bandsyncapi.bandsyncapi.config;

import static org.mockito.Mockito.mock;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import com.bandsyncapi.bandsyncapi.api.v1.services.CustomUserDetailsService;
import com.bandsyncapi.bandsyncapi.api.v1.services.JWTService;
import com.bandsyncapi.bandsyncapi.api.v1.services.MusicalBandsService;

/**
 * This class is used to create test beans for the application that are always needed.
 * due to Spring Security configuration When testing controllers.
 */
@TestConfiguration
public class TestBeansConfig {
  
  @Bean
  JWTService jwtService () {
    return mock(JWTService.class);
  }

  @Bean
  CustomUserDetailsService customUserDetailsService () {
    return mock(CustomUserDetailsService.class);
  }

  @Bean
  MusicalBandsService musicalBandsService () {
    return mock(MusicalBandsService.class);
  }
}
