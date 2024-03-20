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
public class ResetPasswordRequest {

  @NotBlank
  @Size(min = 6, max = 6)
  String verificationCode;
  @NotBlank
  @Size(min = 6)
  String newPassword;
  @NotBlank
  @Email
  private String email;

}
