package com.chiblinx.auth.usecase;

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
public class ChangePasswordRequest {

  @NotBlank
  @Size(min = 6)
  String currentPassword;

  @NotBlank
  @Size(min = 6)
  String newPassword;

}
