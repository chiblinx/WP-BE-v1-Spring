package com.chiblinx.user.service;

import com.chiblinx.user.entity.User;
import com.chiblinx.user.usecase.BasicUserInfo;
import org.springframework.data.domain.Page;

public interface UserService {


  Page<BasicUserInfo> getAllUsers();

  User findByEmail(String email);

}
