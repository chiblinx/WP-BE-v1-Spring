package com.chiblinx.user.service;

import com.chiblinx.core.exceptions.NotFoundException;
import com.chiblinx.user.entity.User;
import com.chiblinx.user.repository.UserRepository;
import com.chiblinx.user.usecase.BasicUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  @Override
  public Page<BasicUserInfo> getAllUsers() {
    return null;
  }

  @Override
  public User findByEmail(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new NotFoundException(String.format("user with email: %s not found", email)));
  }
}
