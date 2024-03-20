package com.chiblinx.auth.service;

import com.chiblinx.auth.usecase.ChangePasswordRequest;
import com.chiblinx.auth.usecase.LoginRequest;
import com.chiblinx.auth.usecase.ResetPasswordRequest;
import com.chiblinx.user.entity.User;

public interface AuthService {

  String[] login(LoginRequest loginRequest) throws Exception;

  String[] refreshToken(String refreshToken) throws Exception;

  void logout(User user);

  String generateVerificationCode(String email) throws Exception;

  void changePassword(ChangePasswordRequest reqData, User user) throws Exception;

  void resetPassword(ResetPasswordRequest reqData) throws Exception;

}
