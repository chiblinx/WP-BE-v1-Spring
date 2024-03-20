package com.chiblinx.auth.controller;

import com.chiblinx.auth.service.AuthService;
import com.chiblinx.auth.usecase.ChangePasswordRequest;
import com.chiblinx.auth.usecase.LoginRequest;
import com.chiblinx.auth.usecase.ResetPasswordRequest;
import com.chiblinx.core.responses.ApiErrorResponse;
import com.chiblinx.core.responses.ApiSuccessResponse;
import com.chiblinx.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Auth")
public class AuthController {

  private final AuthService authService;

  @PostMapping("login")
  @Operation(summary = "Login with valid email/password combinations")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Login Successful",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiSuccessResponse.class))
      ),
      @ApiResponse(
          responseCode = "400",
          description = "Request Validation failures",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))
      ),
      @ApiResponse(
          responseCode = "401",
          description = "Invalid email/password combinations",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))
      ),
      @ApiResponse(
          responseCode = "500",
          description = "Internal Server Error",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))
      )
  })
  ResponseEntity<ApiSuccessResponse<String>> login(@Valid @RequestBody LoginRequest loginRequest)
      throws Exception {
    String[] tokens = authService.login(loginRequest);
    ResponseCookie refreshTokenCookie = ResponseCookie.from("refresh", tokens[1])
        .maxAge(24 * 60 * 60)
        .httpOnly(true)
        .secure(true)
        .sameSite("none")
        .path("/api/v1/auth")
        .build();

    ApiSuccessResponse<String> apiResponse = new ApiSuccessResponse<>("Login Success",
        HttpStatus.OK, tokens[0]);

    return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
        .body(apiResponse);
  }

  @PostMapping("refresh-token")
  @Operation(summary = "Get a new access token from refresh token")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Token Refresh Successful",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseEntity.class))
      ),
      @ApiResponse(
          responseCode = "400",
          description = "Request Validation failures",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))
      ),
      @ApiResponse(
          responseCode = "401",
          description = "Invalid Refresh Token",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))
      ),
      @ApiResponse(
          responseCode = "500",
          description = "Internal Server Error",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))
      )
  })
  ResponseEntity<ApiSuccessResponse<String>> refreshToken(
      @CookieValue(name = "refresh") String refreshToken)
      throws Exception {
    String[] tokens = authService.refreshToken(refreshToken);
    ResponseCookie refreshTokenCookie = ResponseCookie.from("rftk", tokens[1])
        .maxAge(24 * 60 * 60)
        .httpOnly(true)
        .secure(true)
        .sameSite("none")
        .path("/api/v1/auth")
        .build();

    ApiSuccessResponse<String> apiResponse = new ApiSuccessResponse<>("Token Refresh Successful",
        HttpStatus.OK, tokens[0]);

    return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
        .body(apiResponse);
  }

  @PostMapping("logout")
  @SecurityRequirement(name = "bearerAuth")
  @Operation(summary = "Logout Active Auth Session")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Logout Successful"),
      @ApiResponse(
          responseCode = "401",
          description = "Bearer Token missing or has expired",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))
      ),
      @ApiResponse(
          responseCode = "500",
          description = "Internal Server Error",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))
      )
  })
  ResponseEntity<?> logout(Authentication auth) {
    User currentUser = (User) auth.getPrincipal();
    authService.logout(currentUser);

    ResponseCookie refreshTokenCookie = ResponseCookie.from("refresh", "")
        .maxAge(0)
        .httpOnly(true)
        .secure(true)
        .sameSite("none")
        .path("/api/v1/auth")
        .build();

    return ResponseEntity.noContent().header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
        .build();
  }

  @PostMapping("code")
  @Operation(summary = "Get Verification Code")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Verification Code Sent",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiSuccessResponse.class))
      ),
      @ApiResponse(
          responseCode = "400",
          description = "Request Validation failures",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))
      ),
      @ApiResponse(
          responseCode = "404",
          description = "Account with email not found",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))
      ),
      @ApiResponse(
          responseCode = "500",
          description = "Internal Server Error",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))
      )
  })
  ApiSuccessResponse<String> getVerificationCode(
      @Valid @RequestParam(name = "email") @Email String email) throws Exception {
    String code = authService.generateVerificationCode(email);
    return new ApiSuccessResponse<>(String.format("Sent Verification Code is: %s", code),
        HttpStatus.OK);
  }

  @PostMapping("change-password")
  @SecurityRequirement(name = "bearerAuth")
  @Operation(summary = "Change user password, user must be authenticated")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Password Change Successful",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiSuccessResponse.class))
      ),
      @ApiResponse(
          responseCode = "400",
          description = "Request Validation failures",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))
      ),
      @ApiResponse(
          responseCode = "401",
          description = "Bearer Token missing or has expired",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))
      ),
      @ApiResponse(
          responseCode = "403",
          description = "Invalid current password",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))
      ),
      @ApiResponse(
          responseCode = "500",
          description = "Internal Server Error",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))
      )
  })
  ApiSuccessResponse<String> changePassword(@Valid @RequestBody ChangePasswordRequest reqData,
      Authentication auth) throws Exception {
    authService.changePassword(reqData, (User) auth.getPrincipal());
    return new ApiSuccessResponse<>("Password Change Success", HttpStatus.OK);
  }

  @PostMapping("reset-password")
  @Operation(summary = "Password Reset for forgotten passwords")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Password Reset Successful",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiSuccessResponse.class))
      ),
      @ApiResponse(
          responseCode = "400",
          description = "Request Validation failures",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))
      ),
      @ApiResponse(
          responseCode = "401",
          description = "Verification code is invalid or has expired",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))
      ),
      @ApiResponse(
          responseCode = "500",
          description = "Internal Server Error",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))
      )
  })
  ApiSuccessResponse<String> resetPassword(@Valid @RequestBody ResetPasswordRequest reqData)
      throws Exception {
    authService.resetPassword(reqData);
    return new ApiSuccessResponse<>("Password Reset Success", HttpStatus.OK);
  }

}
