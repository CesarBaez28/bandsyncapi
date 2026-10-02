package com.bandsyncapi.bandsyncapi.api.v1.services;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import com.bandsyncapi.bandsyncapi.api.v1.dto.twofa.SetUp2FADto;

/**
 * Service interface for two factor authentication functionality.
 */
public interface TwoFactorService {

  /**
   * Generate a secret key for two factor authentication.
   * 
   * @return - the generated secret key
   */
  public String generateSecretKey();

  /**
   * Validate a two factor authentication code against a secret key.
   * 
   * @param secretKey - the secret key to validate against
   * @param code      - the code to validate
   * @return - true if the code is valid, false otherwise
   */
  public boolean isValidCode(String secretKey, int code);

  /**
   * Get the QR code URL for a given username and secret key.
   * 
   * @param username  - the username to generate the QR code for
   * @param secretKey - the secret key to include in the QR code
   * @return - the generated QR code URL
   */
  public String getQRCodeURL(String username, String secretKey);

  /**
   * Set up two factor authentication for a user.
   * 
   * @param userDetails - the details of the authenticated user
   * @return - a SetUp2FADto containing the encrypted secret key and QR code URL
   *         for the user
   */
  public SetUp2FADto setUp2FA(@AuthenticationPrincipal UserDetails userDetails);

  /**
   * Verify a two factor authentication code for a user.
   * 
   * @param userDetails - the details of the authenticated user
   * @param code        - the code to verify
   * @param secret      - secret
   */
  public void verify2FA(@AuthenticationPrincipal UserDetails userDetails, int code, String secret);

  /**
   * Disable two factor authentication for a user.
   * 
   * @param userDetails - the details of the authenticated user
   */
  public void disable2FA(@AuthenticationPrincipal UserDetails userDetails);
}
