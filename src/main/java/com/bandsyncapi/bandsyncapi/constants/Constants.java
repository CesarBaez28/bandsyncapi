package com.bandsyncapi.bandsyncapi.constants;

/**
 * This class contains constants that are used throughout the application.
 */
public final class Constants {

  private Constants() {
    throw new IllegalStateException("Utility class");
  }

  public static final String ADMIN_ROLE_NAME = "Administrador";
  public static final int MAXIMUN_SIZE_CACHE = 50_000;
  public static final int HOURS_EXPIRATION_CACHE = 12;
  public static final int RATE_LIMIT_CAPACITY = 100;
  public static final int RATE_LIMIT_TOKENS = 100;
  public static final int RATE_LIMIT_MINUTES = 1;
}
