package com.bandsyncapi.bandsyncapi.api.v1.dto.users;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Dto the represents the put request to update a user
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsersPutDto {

  @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres.")
  private String firstName;

  @Size(min = 3, max = 100, message = "El apellido debe tener entre 3 y 100 caracteres.")
  private String lastName;

  @Size(min = 10, max = 100, message = "El número debe tener mínimo 10 dígitos.")
  private String phone;

  private String photo;
}
