package com.chiblinx.user.usecase;

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
public class UpdateUserRequest {

  @Size(min = 2)
  private String firstName;

  @Size(min = 2)
  private String surname;

  @Pattern(regexp = "^\\+?\\d{8,}$")
  private String mobile;

  @Pattern(regexp = "^\\+?\\d{8,}$")
  private String phone;

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

}
