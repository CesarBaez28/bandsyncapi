package com.bandsyncapi.bandsyncapi.config;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import com.bandsyncapi.bandsyncapi.api.v1.services.CustomUserDetailsService;
import com.bandsyncapi.bandsyncapi.api.v1.services.JWTService;
import com.bandsyncapi.bandsyncapi.api.v1.services.MusicalBandsService;
import com.bandsyncapi.bandsyncapi.api.v1.services.RateLimitService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.github.bucket4j.Bucket;

/**
 * This class is used to create test beans for the application that are always
 * needed.
 * due to Spring Security configuration When testing controllers.
 */
@TestConfiguration
public class TestBeansConfig {

  @Bean
  JWTService jwtService() {
    return mock(JWTService.class);
  }

  @Bean
  CustomUserDetailsService customUserDetailsService() {
    return mock(CustomUserDetailsService.class);
  }

  @Bean
  MusicalBandsService musicalBandsService() {
    return mock(MusicalBandsService.class);
  }

  @Bean
  RateLimitService rateLimitService() {
    RateLimitService service = mock(RateLimitService.class);
    Bucket bucket = mock(Bucket.class);

    when(service.resolveBucket(anyString(), anyLong(), anyLong(), anyLong()))
        .thenReturn(bucket);
    when(bucket.tryConsume(1L)).thenReturn(true);

    return service;
  }

  @Bean
  ObjectMapper objectMapper() {
    return new ObjectMapper().registerModule(new JavaTimeModule());
  }
}
