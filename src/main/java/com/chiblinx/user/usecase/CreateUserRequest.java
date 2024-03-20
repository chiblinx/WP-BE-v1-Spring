package com.chiblinx.user.usecase;

import com.chiblinx.core.validators.enums.ValueOfEnum;
import com.chiblinx.user.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest {

  @NotBlank
  @Email
  private String email;

  @NotBlank
  @Size(min = 6)
  private String password;

  @NotBlank
  @Size(min = 2)
  private String firstName;

  @NotBlank
  @Size(min = 2)
  private String surname;

  @NotBlank
  @Pattern(regexp = "^\\+?\\d{8,}$")
  private String mobile;

  @Pattern(regexp = "^\\+?\\d{8,}$")
  private String phone;

  @NotNull
  @Past
  private LocalDate dateOfBirth;

  @Size(min = 2)
  private String aboutMe;

  @Size(min = 2)
  private String profilePhoto;

  @Size(min = 2)
  private String facebook;

  @Size(min = 2)
  private String twitter;

  @Size(min = 2)
  private String linkedin;

  @Size(min = 2)
  private String instagram;

  @Size(min = 2)
  private String website;

  @ValueOfEnum(enumClass = Role.class)
  private String role;

}
