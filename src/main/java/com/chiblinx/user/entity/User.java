package com.chiblinx.user.entity;

import com.chiblinx.core.usecase.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users", indexes = {
    @Index(name = "idx_email", columnList = "email")
})
public class User extends BaseEntity implements UserDetails {

  @Column(unique = true, updatable = false, nullable = false)
  private String email;

  @JsonIgnore
  @Column(nullable = false)
  private String password;

  private String firstName;

  private String surname;

  @Column(unique = true)
  private String phone;

  @Column(unique = true)
  private String mobile;

  @Column(columnDefinition = "datetime")
  private LocalDate dateOfBirth;

  @Column(columnDefinition = "text")
  private String aboutMe;

  private String profilePhoto;

  private String facebook;

  private String twitter;

  private String linkedin;

  private String instagram;

  private String website;

  @Column
  private LocalDateTime lastLogin;

  @Column(name = "refresh_token", columnDefinition = "text")
  private String refreshToken;

  @Column
  private String code;

  @Column(name = "code_expiry")
  private Long codeExpiry;

  @Builder.Default
  @Column(columnDefinition = "boolean default true")
  private Boolean isAccountNonExpired = true;

  @Builder.Default
  @Column(columnDefinition = "boolean default true")
  private Boolean isAccountNonLocked = true;

  @Builder.Default
  @Column(columnDefinition = "boolean default true")
  private Boolean isCredentialsNonExpired = true;

  @Builder.Default
  @Column(columnDefinition = "boolean default true")
  private Boolean isEnabled = true;

  @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH,
      CascadeType.DETACH})
  @JoinColumn(name = "user_role_id")
  private UserRole role;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(role.getRoleName().name()));
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return isAccountNonExpired;
  }

  @Override
  public boolean isAccountNonLocked() {
    return isAccountNonLocked;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return isCredentialsNonExpired;
  }

  @Override
  public boolean isEnabled() {
    return isEnabled;
  }
}
