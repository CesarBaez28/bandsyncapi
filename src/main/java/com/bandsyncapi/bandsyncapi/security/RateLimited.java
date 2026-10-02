package com.bandsyncapi.bandsyncapi.security;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom anotation to specify rate limit for endpoints
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimited {

  long capacity();

  long refillTokens();

  long refillMinutes();
}
