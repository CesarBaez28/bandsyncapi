package com.bandsyncapi.bandsyncapi.constants;

/**
 * This class contains the names of the caches used in the application.
 * 
 */
public final class CacheNames {

  private CacheNames() {
    throw new IllegalStateException("Utility class");
  }

  public static final String MUSICAL_GENRES_ALL = "musicalGenresAll";
  public static final String MUSICAL_GENRES_BY_BAND = "musicalGenresByBand";
  public static final String MUSICAL_BANDS_BY_ID = "musicalBandsById";
  public static final String MUSICAL_BANDS_BY_HYPHENATED_NAME = "musicalBandsByHyphenatedName";
  public static final String MUSICAL_ROLES_BY_BAND = "musicalRolesByBand";
  public static final String EVENTS_BY_BAND = "eventsByBand";
  public static final String ARTISTS_BY_BAND = "artistsByBand";
  public static final String PERMISSIONS_ALL = "permissionsAll";
  public static final String PERMISSIONS_BY_ID = "permissionsById";
  public static final String PERMISSION_TYPES_ALL = "permissionTypesAll";
  public static final String ROLES_ALL = "rolesAll";
}