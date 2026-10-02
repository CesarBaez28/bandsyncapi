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
   * Encrypts the given text
   * 
   * @param text - the text to be encrypted
   * @return the encrypted text
   */
  public String encrypt(String text) {
    return encoder.encode(text);
  }

  /**
   * Checks if the password matches the hash
   * 
   * @param password - the password to be checked
   * @param hash     - the hash to be checked against
   * @return true if the password matches the hash, false otherwise
   */
  public boolean checkPassword(String password, String hash) {
    return encoder.matches(password, hash);
  }
}
