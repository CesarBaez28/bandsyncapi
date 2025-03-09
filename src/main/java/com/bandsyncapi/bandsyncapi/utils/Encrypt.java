package com.bandsyncapi.bandsyncapi.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * This class is used to encrypt passwords
 */
@Component
public class Encrypt {

  private final PasswordEncoder encoder;

  /**
   * Constructor for the Encrypt class
   * 
   */
  public Encrypt() {
    encoder = new BCryptPasswordEncoder();
  }

  /**
   * Encrypts the password
   * 
   * @param password - the password to be encrypted
   * @return the encrypted password
   */
  public String encryptPassword(String password) {
    return encoder.encode(password);
  }

  /**
   * Checks if the password matches the hash
   * 
   * @param password - the password to be checked
   * @param hash - the hash to be checked against
   * @return true if the password matches the hash, false otherwise
   */
  public boolean checkPassword(String password, String hash) {
    return encoder.matches(password, hash);
  }
}
