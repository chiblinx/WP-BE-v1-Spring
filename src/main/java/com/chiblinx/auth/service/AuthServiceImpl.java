package com.chiblinx.auth.service;

import com.chiblinx.auth.usecase.ChangePasswordRequest;
import com.chiblinx.auth.usecase.LoginRequest;
import com.chiblinx.auth.usecase.ResetPasswordRequest;
import com.chiblinx.core.exceptions.ForbiddenException;
import com.chiblinx.core.exceptions.UnauthorizedException;
import com.chiblinx.core.security.JwtService;
import com.chiblinx.core.services.email.EmailService;
import com.chiblinx.core.services.email.usecase.EmailDetails;
import com.chiblinx.core.utils.EncryptionUtil;
import com.chiblinx.user.entity.User;
import com.chiblinx.user.repository.UserRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private static final long CODE_EXPIRY_DURATION = 5 * 60 * 1000; // 5 minutes
  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final EncryptionUtil encryptionUtil;
  private final EmailService emailService;

  @Override
  public String[] login(LoginRequest loginRequest) throws Exception {
    log.info("new login request with email: [{}]", loginRequest.getEmail());

    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            loginRequest.getEmail(),
            loginRequest.getPassword()
        )
    );

    final User user = findUserByEmail(loginRequest.getEmail());
    return generateTokens(user);
  }

  @Override
  public String[] refreshToken(String refreshToken) throws Exception {
    String email = jwtService.extractUsername(refreshToken);
    final User user = findUserByEmail(email);

    final String refreshTokenHash = encryptionUtil.generateSHA256Hash(refreshToken);
    final boolean isRefreshTokenValid = jwtService.isTokenValid(refreshToken, user);

    if (!user.getRefreshToken().equals(refreshTokenHash) || !isRefreshTokenValid) {
      log.error(":: invalid refresh token ::");
      throw new UnauthorizedException();
    }

    return generateTokens(user);
  }

  @Override
  public void logout(User user) {
    log.info("logout request with email: {}", user.getEmail());
    userRepository.invalidateRefreshToken("", user.getEmail());
  }

  @Override
  public String generateVerificationCode(String email) throws Exception {
    User user = findUserByEmail(email);

    String code = RandomStringUtils.randomAlphanumeric(6).toUpperCase();

    String codeHash = encryptionUtil.generateSHA256Hash(code);

    userRepository.updateCode(codeHash, System.currentTimeMillis(), user.getEmail());

    String msgBody = String.format(
        "Dear User, Thank you for using the property market place platform "
            + "This is your verification code: %s expires in five minutes", code);

    EmailDetails emailDetails = EmailDetails.builder()
        .recipient(email)
        .msgBody(msgBody)
        .subject("VERIFICATION CODE")
        .build();

    emailService.sendSimpleMail(emailDetails);

    return code;
  }

  @Override
  public void changePassword(ChangePasswordRequest reqData, User user) throws Exception {
    if (!passwordEncoder.matches(reqData.getCurrentPassword(), user.getPassword())) {
      log.error("passwords do not match");
      throw new ForbiddenException();
    }

    String encodedPassword = passwordEncoder.encode(reqData.getNewPassword());
    userRepository.updatePassword(encodedPassword, user.getId());

    log.info("password change successful");
  }

  @Override
  public void resetPassword(ResetPasswordRequest reqData) throws Exception {
    User user = findUserByEmail(reqData.getEmail());

    String codeHash = encryptionUtil.generateSHA256Hash(reqData.getVerificationCode());
    boolean isCodeExpired =
        System.currentTimeMillis() - user.getCodeExpiry() > CODE_EXPIRY_DURATION;

    if (!user.getCode().equals(codeHash) || isCodeExpired) {
      log.error("invalid verification code");
      throw new UnauthorizedException();
    }

    String encodedPassword = passwordEncoder.encode(reqData.getNewPassword());
    userRepository.updatePassword(encodedPassword, user.getId());
  }

  private User findUserByEmail(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new UnauthorizedException("UNAUTHORIZED"));
  }

  private String[] generateTokens(User user) throws Exception {
    String accessToken = jwtService.generateToken(user, 1000 * 60 * 60);
    String refreshToken = jwtService.generateToken(user, 1000 * 24 * 60 * 60);

    String[] tokens = new String[2];
    tokens[0] = accessToken;
    tokens[1] = refreshToken;

    String refreshTokenHash = encryptionUtil.generateSHA256Hash(refreshToken);

    LocalDateTime currentTime = LocalDateTime.now();
    userRepository.updateLastLogin(currentTime, refreshTokenHash, user.getEmail());

    return tokens;
  }
}
