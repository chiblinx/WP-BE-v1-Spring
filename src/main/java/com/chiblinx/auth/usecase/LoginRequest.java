package com.chiblinx.auth.usecase;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

  @NotBlank
  @Email
  private String email;

  @NotBlank
  @Size(min = 6)
  private String password;

}
