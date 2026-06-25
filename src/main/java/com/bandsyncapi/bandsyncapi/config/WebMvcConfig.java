package com.bandsyncapi.bandsyncapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.bandsyncapi.bandsyncapi.security.RateLimitInterceptor;

/**
 * Interceptor for rate limit
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  private final RateLimitInterceptor rateLimitInterceptor;

  /**
   * Constructor to set rate limit interceptor
   * 
   * @param rateLimitInterceptor - Rate limit interceptor
   */
  public WebMvcConfig(RateLimitInterceptor rateLimitInterceptor) {
    this.rateLimitInterceptor = rateLimitInterceptor;
  }

  @Override
  public void addInterceptors(@NonNull InterceptorRegistry registry) {
    if (rateLimitInterceptor != null) {
      registry.addInterceptor(rateLimitInterceptor);
    }
  }
}
