package com.bandsyncapi.bandsyncapi.api.v1.services.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.bandsyncapi.bandsyncapi.api.v1.services.EmailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

/**
 * EmailService implementations
 */
@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

  private final JavaMailSender javaMailSender;

  private final TemplateEngine templateEngine;

  @Value("${spring.application.name}")
  private String appName;

  @Value("${app.email}")
  private String emailFrom;

  /**
   * Constructor
   * 
   * @param javaMailSender - to send email
   * @param templateEngine - to process html template
   */
  public EmailServiceImpl(JavaMailSender javaMailSender, TemplateEngine templateEngine) {
    this.javaMailSender = javaMailSender;
    this.templateEngine = templateEngine;
  }

  @Override
  public void sendInvitationEmail(String to, String bandName, String invitedBy, String invitationLink,
      LocalDateTime expirationDate) throws MessagingException {
    log.info("Sending email to: {}", to);

    MimeMessage message = javaMailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

    Context context = new Context();
    context.setVariables(Map.of(
        "bandName", bandName,
        "invitedBy", invitedBy,
        "invitationLink", invitationLink,
        "expirationDate", expirationDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
        "currentYear", String.valueOf(java.time.LocalDate.now().getYear()),
        "appName", appName));

    String htmlContent = templateEngine.process("emails/invitation", context);

    helper.setTo(to);
    helper.setSubject("Invitación para unirte a la banda " + bandName);
    helper.setText(htmlContent, true);

    helper.setFrom(emailFrom);

    javaMailSender.send(message);
    log.info("Invitation email send successfully to email: {}", to);
  }

  @Override
  public void sendResetPasswordEmail(String to, String resetLink) throws MessagingException {
    log.info("Sending reset password email to: {}", to);

    MimeMessage message = javaMailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

    Context context = new Context();
    context.setVariable("resetLink", resetLink);
    context.setVariable("appName", appName);

    String htmlContent = templateEngine.process("emails/reset-password", context);

    helper.setTo(to);
    helper.setSubject("Recuperación de contraseña");
    helper.setText(htmlContent, true);

    helper.setFrom(emailFrom);

    javaMailSender.send(message);
    log.info("Reset password email send successfully to email: {}", to);
  }
}