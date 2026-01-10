package com.bandsyncapi.bandsyncapi.api.v1.constants;

/**
 * Centralized definition of user permissions.
 * These values are used in @PreAuthorize annotations.
 */
public final class UserPermissions {

  private UserPermissions() {
    // Prevent instantiation
  }

  // =========================
  // Members (Integrantes)
  // =========================
  public static final String VIEW_MEMBERS = "Visualizar integrantes";
  public static final String ADD_MEMBER = "Agregar integrante";
  public static final String UPDATE_MEMBER = "Modificar integrante";
  public static final String DELETE_MEMBER = "Eliminar integrante";

  // =========================
  // Repertoires (Repertorio)
  // =========================
  public static final String ADD_REPERTOIRE = "Agregar repertorio";
  public static final String UPDATE_REPERTOIRE = "Modificar repertorio";
  public static final String DELETE_REPERTOIRE = "Eliminar repertorio";

  // =========================
  // Events (Eventos)
  // =========================
  public static final String ADD_EVENT = "Agregar evento";
  public static final String UPDATE_EVENT = "Modificar evento";
  public static final String DELETE_EVENT = "Eliminar evento";

  // =========================
  // Artists (Artistas)
  // =========================
  public static final String ADD_ARTIST = "Agregar artista";
  public static final String UPDATE_ARTIST = "Modificar artista";
  public static final String DELETE_ARTIST = "Eliminar artista";

  // =========================
  // Songs (Canciones)
  // =========================
  public static final String ADD_SONG = "Agregar cancion";
  public static final String UPDATE_SONG = "Modificar cancion";
  public static final String DELETE_SONG = "Eliminar cancion";

  // =========================
  // Roles & Permissions
  // =========================
  public static final String VIEW_ROLES_AND_PERMISSIONS = "Visualizar roles y permisos";
  public static final String ASSIGN_ROLE = "Asignar rol";
  public static final String ADD_ROLE = "Agregar rol";
  public static final String UPDATE_ROLE = "Modificar rol";
  public static final String DELETE_ROLE = "Eliminar rol";

  // =========================
  // Musical Genres (Géneros musicales)
  // =========================
  public static final String ADD_MUSICAL_GENRE = "Agregar género musical";
  public static final String UPDATE_MUSICAL_GENRE = "Modificar género musical";
  public static final String DELETE_MUSICAL_GENRE = "Eliminar género musical";

  // =========================
  // Musical Roles (Roles musicales)
  // =========================
  public static final String ADD_MUSICAL_ROLE = "Agregar rol musical";
  public static final String UPDATE_MUSICAL_ROLE = "Modificar rol musical";
  public static final String DELETE_MUSICAL_ROLE = "Eliminar rol musical";
}
