package com.chiblinx.user.service;

import com.chiblinx.user.entity.User;
import com.chiblinx.user.enums.Role;
import com.chiblinx.user.usecase.BasicUserInfo;
import com.chiblinx.user.usecase.CreateUserRequest;
import com.chiblinx.user.usecase.UpdateUserRequest;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;

public interface UserService {


  Page<BasicUserInfo> getAllUsers(String searchTerm, LocalDateTime startDate, LocalDateTime endDate,
      int page, int size);

  User getUserByEmail(String email);

  User getUserById(String id);

  User createUser(CreateUserRequest reqData);

  void updateUserRole(String id, Role role);

  User updateUser(String id, UpdateUserRequest reqData);

  void deleteUser(String id, User user);

}
