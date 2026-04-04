package com.bandsyncapi.bandsyncapi.api.v1.services.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.bandsyncapi.bandsyncapi.api.v1.dto.twofa.SetUp2FADto;
import com.bandsyncapi.bandsyncapi.api.v1.models.UsersModel;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.UsersRepository;
import com.bandsyncapi.bandsyncapi.api.v1.services.TwoFactorService;
import com.bandsyncapi.bandsyncapi.utils.AESUtil;
import com.warrenstrange.googleauth.GoogleAuthenticator;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of TwoFactorService
 */
@Service
@Slf4j
public class TwoFactorServiceImpl implements TwoFactorService {

  private final GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();

  private final UsersRepository usersRepository;

  private final AESUtil aesUtil;


  @Value("${spring.application.name}")
  private String appName;

  public TwoFactorServiceImpl(UsersRepository usersRepository, AESUtil aesUtil) {
    this.usersRepository = usersRepository;
    this.aesUtil = aesUtil;
  }

  @Override
  public String generateSecretKey() {
    return googleAuthenticator.createCredentials().getKey();
  }

  @Override
  public boolean isValidCode(String secretKey, int code) {
    return googleAuthenticator.authorize(secretKey, code);
  }

  @Override
  public String getQRCodeURL(String username, String secretKey) {
    return "otpauth://totp/" + appName + ":" + username +
        "?secret=" + secretKey +
        "&issuer=" + appName;
  }

  @Override
  public SetUp2FADto setUp2FA(@AuthenticationPrincipal UserDetails userDetails) {
    log.info("Setting up 2FA for user: {}", userDetails.getUsername());

    String username = userDetails.getUsername();

    String secret = generateSecretKey();
    String encryptedSecret = aesUtil.encrypt(secret);
    String qrCodeURL = getQRCodeURL(username, secret);

    var setUp2FADto = SetUp2FADto.builder()
        .secret(encryptedSecret)
        .qrUrl(qrCodeURL)
        .build();

    log.info("Generated secret key for user {}: {}", username, secret);
    return setUp2FADto;
  }

  @Override
  public void verify2FA(UserDetails userDetails, int code, String secret) {
    log.info("Verifying 2FA code for user: {}", userDetails.getUsername());

    String username = userDetails.getUsername();

    UsersModel user = usersRepository.findByUsername(username)
        .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));

    String decryptedSecret = aesUtil.decrypt(secret);

    if (!isValidCode(decryptedSecret, code)) {
      log.warn("Invalid 2FA code for user: {}", username);
      throw new IllegalArgumentException("Invalid 2FA code");
    }

    user.setIs2FAEnabled(true);
    user.setSecret2FA(secret);
    usersRepository.save(user);
    
    log.info("2FA enabled successfully for user: {}", username);
  }

  @Override
  public void disable2FA(UserDetails userDetails) {
    log.info("Disabling 2FA for user: {}", userDetails.getUsername());

    String username = userDetails.getUsername();

    UsersModel user = usersRepository.findByUsername(username)
        .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));

    user.setSecret2FA(null);
    user.setIs2FAEnabled(false);
    usersRepository.save(user);

    log.info("2FA disabled successfully for user: {}", username);
  }
}
