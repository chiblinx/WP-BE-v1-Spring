package com.chiblinx.user.entity;

import com.chiblinx.core.usecase.entity.BaseEntity;
import com.chiblinx.user.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_roles")
public class UserRole extends BaseEntity {

  @Enumerated(EnumType.STRING)
  private Role roleName;

  @Column(columnDefinition = "text")
  private String description;

}
