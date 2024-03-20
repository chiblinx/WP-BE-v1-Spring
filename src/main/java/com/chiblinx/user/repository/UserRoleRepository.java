package com.chiblinx.user.repository;

import com.chiblinx.user.entity.UserRole;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRoleRepository extends JpaRepository<UserRole, UUID> {

  @Query(value = "SELECT * FROM user_roles WHERE role_name = CAST(:roleName as role)", nativeQuery = true)
  Optional<UserRole> findByRoleName(@Param("roleName") String roleName);
}
