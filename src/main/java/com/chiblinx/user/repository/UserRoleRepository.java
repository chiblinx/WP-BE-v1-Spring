package com.chiblinx.user.repository;

import com.chiblinx.user.entity.UserRole;
import com.chiblinx.user.enums.Role;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, UUID> {

  Optional<UserRole> findByRoleName(Role roleName);

}
