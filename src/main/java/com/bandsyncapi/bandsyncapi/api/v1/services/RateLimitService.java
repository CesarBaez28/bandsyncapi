package com.bandsyncapi.bandsyncapi.api.v1.services;

import io.github.bucket4j.Bucket;

/**
 * Rate limit service
 */
public interface RateLimitService {

  /**
   * Resolve the cache bucket
   * 
   * @param key           - cache key
   * @param capacity      - capacity in terms of Token-Bucket
   * @param refillTokens  - amount of tokens
   * @param refillMinutes - the period within tokens will be fully regenerated
   * @return - The cache bucket
   */
  Bucket resolveBucket(String key, long capacity, long refillTokens, long refillMinutes);
}