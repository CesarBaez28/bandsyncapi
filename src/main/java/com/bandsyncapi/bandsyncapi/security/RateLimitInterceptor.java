package com.bandsyncapi.bandsyncapi.security;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.bandsyncapi.bandsyncapi.api.v1.services.RateLimitService;
import com.bandsyncapi.bandsyncapi.exceptions.RateLimitExceededException;

import io.github.bucket4j.Bucket;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * RateLimitInterceptor
 */
@Component
public class RateLimitInterceptor implements HandlerInterceptor {

  private final RateLimitService rateLimitService;

  public RateLimitInterceptor(
      RateLimitService rateLimitService) {
    this.rateLimitService = rateLimitService;
  }

  @Override
  public boolean preHandle(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull Object handler)
      throws Exception {

    if (!(handler instanceof HandlerMethod handlerMethod)) {
      return true;
    }

    RateLimited rateLimited = AnnotationUtils.findAnnotation(
        handlerMethod.getMethod(),
        RateLimited.class);

    if (rateLimited == null) {
      rateLimited = AnnotationUtils.findAnnotation(
          handlerMethod.getBeanType(),
          RateLimited.class);
    }

    if (rateLimited == null) {
      return true;
    }

    String clientIp = extractClientIp(request);

    String bucketKey = clientIp +
        ":" +
        handlerMethod.getMethod().toGenericString() +
        ":" +
        rateLimited.capacity() +
        ":" +
        rateLimited.refillTokens() +
        ":" +
        rateLimited.refillMinutes();

    Bucket bucket = rateLimitService.resolveBucket(
        bucketKey,
        rateLimited.capacity(),
        rateLimited.refillTokens(),
        rateLimited.refillMinutes());

    boolean allowed = bucket.tryConsume(1);
    
    if (!allowed) {
      throw new RateLimitExceededException("Too many requests. Please try again later");
    }

    return allowed;
  }

  private String extractClientIp(HttpServletRequest request) {

    String forwarded = request.getHeader("X-Forwarded-For");

    if (forwarded != null &&
        !forwarded.isBlank() &&
        !"unknown".equalsIgnoreCase(forwarded)) {

      return forwarded.split(",")[0].trim();
    }

    return request.getRemoteAddr();
  }
}
