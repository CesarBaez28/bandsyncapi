package com.bandsyncapi.bandsyncapi.api.v1.services.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bandsyncapi.bandsyncapi.api.v1.models.PasswordResetTokenModel;
import com.bandsyncapi.bandsyncapi.api.v1.models.UsersModel;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.PasswordResetTokenRepository;
import com.bandsyncapi.bandsyncapi.api.v1.repositories.UsersRepository;
import com.bandsyncapi.bandsyncapi.api.v1.services.EmailService;
import com.bandsyncapi.bandsyncapi.api.v1.services.PasswordResetTokenService;
import com.bandsyncapi.bandsyncapi.exceptions.ResetPasswordException;
import com.bandsyncapi.bandsyncapi.utils.Encrypt;

import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

/**
 * PasswordResetTokenService implementation
 */
@Service
@Slf4j
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {

  private final UsersRepository usersRepository;

  private final PasswordResetTokenRepository passwordResetTokenRepository;

  private final EmailService emailService;

  private final Encrypt encrypt;

  @Value("${fronted.url}")
  private String frontedUrl;

  /**
   * Constructor
   * 
   * @param usersRepository              - The UsersRepository to access user data
   * @param passwordResetTokenRepository - The PasswordResetTokenRepository to
   *                                     access password reset token data
   * @param emailService                 - The EmailService to send emails to
   *                                     users
   * @param encrypt                      - The Encrypt utility to encrypt
   *                                     passwords and check password matches
   */
  public PasswordResetTokenServiceImpl(UsersRepository usersRepository,
      PasswordResetTokenRepository passwordResetTokenRepository, EmailService emailService, Encrypt encrypt) {
    this.usersRepository = usersRepository;
    this.passwordResetTokenRepository = passwordResetTokenRepository;
    this.emailService = emailService;
    this.encrypt = encrypt;
  }

  @Override
  public void forgotPassword(String email) throws MessagingException {
    Optional<UsersModel> userOpt = usersRepository.findByEmail(email);

    // No revelar si el usuario existe
    if (userOpt.isEmpty()) {
      return;
    }

    UsersModel user = userOpt.get();

    // Eliminar tokens anteriores
    passwordResetTokenRepository.deleteByUserId(user.getId());

    var resetToken = PasswordResetTokenModel.builder()
        .user(user)
        .expirationDate(LocalDateTime.now().plusMinutes(15))
        .build();

    PasswordResetTokenModel savedToken = passwordResetTokenRepository.save(resetToken);

    String resetLink = frontedUrl + "/reset-password?token=" + savedToken.getToken();

    emailService.sendResetPasswordEmail(user.getEmail(), resetLink);
  }

  @Override
  public void resetPassword(java.util.UUID token, String newPassword) {
    PasswordResetTokenModel resetToken = passwordResetTokenRepository.findByToken(token)
        .orElseThrow(() -> new EntityNotFoundException("El token utilizado para resetear la contraseña no existe o es inválido"));

    if (resetToken.isUsed()) {
      throw new ResetPasswordException("Este token ya ha sido utilizado para resetear la contraseña");
    }

    if (resetToken.getExpirationDate().isBefore(LocalDateTime.now())) {
      throw new ResetPasswordException("Este token ha expirado. Por favor, solicita un nuevo enlace de recuperación de contraseña");
    }

    UsersModel user = resetToken.getUser();

    user.setPassword(encrypt.encrypt(newPassword));
    usersRepository.save(user);

    // Marcar token como usado
    resetToken.setUsed(Boolean.TRUE);
    passwordResetTokenRepository.save(resetToken);
  }
}
