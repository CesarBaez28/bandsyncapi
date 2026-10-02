package com.bandsyncapi.bandsyncapi.api.v1.services;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bandsyncapi.bandsyncapi.api.v1.models.UsersModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.UsersRolesModel;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.RolesPermissionsRepository;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.UsersRepository;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.UsersRolesRepository;

/**
 * Custom implementation of UserDetailsService for loading user details.
 * 
 * This class is responsible for retrieving user information from the database.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

  private final UsersRepository usersRepository;

  private final UsersRolesRepository usersRolesRepository;

  private final RolesPermissionsRepository rolesPermissionsRepository;

  /**
   * Constructor for CustomUserDetailsService.
   *
   * @param usersRepository      the UsersRepository to use for retrieving user
   *                             information
   * @param usersRolesRepository the UsersRolesRepository to use for retrieving
   *                             user roles
   * @param rolesPermissionsRepository the RolesPermissionsRepository to use for
   *                             retrieving role permissions
   */
  public CustomUserDetailsService(UsersRepository usersRepository, UsersRolesRepository usersRolesRepository,
      RolesPermissionsRepository rolesPermissionsRepository) {
    this.usersRepository = usersRepository;
    this.usersRolesRepository = usersRolesRepository;
    this.rolesPermissionsRepository = rolesPermissionsRepository;
  }

  /**
   * Loads user details by username.
   *
   * @param username the username of the user to load
   * @return UserDetails object containing user information
   * @throws UsernameNotFoundException if the user is not found
   */
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    UsersModel user = usersRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

    return new User(user.getUsername(), user.getPassword(), Collections.emptyList());
  }

  /**
   * Loads user details by username and musical band ID.
   *
   * @param username      the username of the user to load
   * @param musicalBandId the ID of the musical band
   * @return UserDetails object containing user information
   * @throws UsernameNotFoundException if the user is not found
   */
  public UserDetails loadUserByUsernameAndMusicalBandId(String username, UUID musicalBandId)
      throws UsernameNotFoundException {

    UsersModel user = usersRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException(
            String.format("User not found with username: %s", username)));

    // Get the user's role for the specified musical band
    Optional<UsersRolesModel> optionalUserRole = usersRolesRepository.findByUserIdAndMusicalBandId(user.getId(),
        musicalBandId);

    // If the user has no roles for the specified musical band, return an empty
    // authority list
    if (optionalUserRole.isEmpty()) {
      return new User(user.getUsername(), user.getPassword(), Collections.emptyList());
    }

    UsersRolesModel usersRolesModel = optionalUserRole.get();

    // Retrieve permissions and map to GrantedAuthority
    Collection<GrantedAuthority> authorities = rolesPermissionsRepository.findAllByRole(usersRolesModel.getRole().getId())
        .stream()
        .map(permission -> new SimpleGrantedAuthority("ROLE_" + permission.getPermission().getName()))
        .collect(Collectors.toSet());

    return new User(user.getUsername(), user.getPassword(), authorities);
  }
}
