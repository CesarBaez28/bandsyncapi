package com.bandsyncapi.bandsyncapi.api.v1.services;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.bandsyncapi.bandsyncapi.api.v1.dto.users.UserLoginPostDto;
import com.bandsyncapi.bandsyncapi.api.v1.dto.users.UsersPutDto;
import com.bandsyncapi.bandsyncapi.api.v1.models.UsersModel;

/**
 * Interface that defines methods for UsersModel
 */
public interface UsersService {

  /**
   * Authenticate a user
   * 
   * @param userLoginPostDto - A UserLoginPostDto object
   * @return - true if the user is authenticated, false otherwise
   */
  boolean verify (UserLoginPostDto userLoginPostDto);

  /**
   * register a user
   * 
   * @param usersModel - A UsersModel object
   * @return - The new user
   */
  UsersModel register (UsersModel usersModel);

  /**
   * Join a user to a musical band
   * 
   * @param userId - User id
   * @param musicalBandId - Musical Band id
   */
  void joinUserToMusicalBand(UUID userId, UUID musicalBandId);

  /**
   * Get all users that are part of a musical band
   * 
   * @param musicalBandId - Musical Band id
   * @return - A UsersModel List
   */
  List<UsersModel> getAllUsersByMusicalBandId(UUID musicalBandId); 


  /**
   * find all users that are part of a musical band and by
   * username, email, firstname, lastanme and phone number
   * 
   * @param musicalBandId - musical band id
   * @param term - search term
   * @param page - page 
   * @param size -  size
   * @return Page of UsersModel
   */
  Page<UsersModel> find (UUID musicalBandId, String term, int page, int size);

  /**
   * Get user by Id
   * 
   * @param userId - user id
   * @return UsersModel object
   */
  UsersModel getById (UUID userId);
  
  /**
   * Check if the user exists by email
   * 
   * @param email - email
   * @return boolean
   */
  boolean existsByEmail(String email);

  /**
   * Update a user info
   * 
   * @param userId - user id
   * @param usersPutDto - A UsersPutDto with the data to be updated
   * @param image - User image
   * @return updated user info
   */
  UsersPutDto updateUser (UUID userId, UsersPutDto usersPutDto, MultipartFile image) throws IOException;

  /**
   * Get user by username
   * 
   * @param username - username
   * @return UsersModel object
   */
  UsersModel getByUsername(String username);
}
