package com.bandsyncapi.bandsyncapi.api.v1.models;

import java.time.LocalDateTime;
import java.util.UUID;

import com.bandsyncapi.bandsyncapi.api.v1.enums.InvitationStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This the model for the invitations table in the database
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "invitations", indexes = {
    @Index(name = "idx_invitations_email_band_status", columnList = "email, musical_band_id, status"),
    @Index(name = "idx_invitations_created", columnList = "created_at")
}, uniqueConstraints = {
    @UniqueConstraint(name = "uk_invitations_token", columnNames = "token")
})
public class InvitationsModel {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Email
  @NotBlank
  @Column(name = "email", nullable = false, length = 255)
  private String email;

  @ManyToOne
  @JoinColumn(name = "musical_band_id", nullable = false)
  private MusicalBandsModel musicalBand;

  @NotBlank
  @Column(name = "token", nullable = false, length = 500, unique = true)
  private String token;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 20)
  @Builder.Default
  private InvitationStatus status = InvitationStatus.PENDING;

  @ManyToOne
  @JoinColumn(name = "invited_by", nullable = false)
  private UsersModel invitedBy;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "expires_at")
  private LocalDateTime expiresAt;
}
