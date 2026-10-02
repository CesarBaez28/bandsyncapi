package com.bandsyncapi.bandsyncapi.api.v1.models;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class is used to store the password reset tokens for the users
 */
@Entity
@Table(name = "password_reset_tokens")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordResetTokenModel {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID token;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private UsersModel user;

  @Column(name = "expiration_date", nullable = false)
  private LocalDateTime expirationDate;

  @Column(name = "used", nullable = false)
  @Builder.Default
  private boolean used = false;
}
