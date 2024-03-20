package com.chiblinx.user.repository;

import com.chiblinx.user.entity.UserRole;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, UUID> {

}
