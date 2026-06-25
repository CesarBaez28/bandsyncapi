package com.bandsyncapi.bandsyncapi.api.v1.services.impl;

import java.time.Duration;

import org.springframework.stereotype.Service;

import com.bandsyncapi.bandsyncapi.api.v1.services.RateLimitService;
import com.bandsyncapi.bandsyncapi.constants.Constants;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of RateLimitService
 */
@Service
@Slf4j
public class RateLimitServiceImpl implements RateLimitService {

  private final Cache<String, Bucket> buckets;

  /**
   * Constructor to set the buckets
   */
  public RateLimitServiceImpl() {
    this.buckets = Caffeine.newBuilder()
        .maximumSize(Constants.MAXIMUN_SIZE_CACHE)
        .expireAfterAccess(Duration.ofHours(Constants.HOURS_EXPIRATION_CACHE))
        .build();
  }

  @Override
  public Bucket resolveBucket(String key, long capacity, long refillTokens, long refillMinutes) {
    return buckets.get(
        key,
        k -> createBucket(
            capacity,
            refillTokens,
            refillMinutes));
  }

  private Bucket createBucket(
      long capacity,
      long refillTokens,
      long refillMinutes) {

    Bandwidth limit = Bandwidth.builder()
        .capacity(capacity)
        .refillGreedy(refillTokens, Duration.ofMinutes(refillMinutes))
        .build();

    return Bucket.builder()
        .addLimit(limit)
        .build();
  }
}
