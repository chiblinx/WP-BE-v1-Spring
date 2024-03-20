package com.chiblinx.user.controller;

import com.chiblinx.core.responses.ApiErrorResponse;
import com.chiblinx.core.responses.ApiSuccessResponse;
import com.chiblinx.user.entity.User;
import com.chiblinx.user.enums.Role;
import com.chiblinx.user.service.UserService;
import com.chiblinx.user.usecase.BasicUserInfo;
import com.chiblinx.user.usecase.CreateUserRequest;
import com.chiblinx.user.usecase.UpdateUserRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
@Tag(name = "User")
public class UserController {

  private final UserService userService;

  @GetMapping
  @SecurityRequirement(name = "bearerAuth")
  @Operation(summary = "Get all Users")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "All Users Returned",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiSuccessResponse.class))
      ),
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
  ApiSuccessResponse<Page<BasicUserInfo>> getAllUsers(
      @RequestParam(name = "searchText", required = false) String searchText,
      @RequestParam(name = "startDate", required = false) @DateTimeFormat LocalDateTime startDate,
      @RequestParam(name = "endDate", required = false) @DateTimeFormat LocalDateTime endDate,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {
    return new ApiSuccessResponse<>("all users", HttpStatus.OK,
        userService.getAllUsers(searchText, startDate, endDate, page, size));
  }

  @GetMapping("/{id}")
  @SecurityRequirement(name = "bearerAuth")
  @Operation(summary = "Get User by ID")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "User with specified ID returned",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiSuccessResponse.class))
      ),
      @ApiResponse(
          responseCode = "401",
          description = "Bearer Token missing or has expired",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))
      ),
      @ApiResponse(
          responseCode = "404",
          description = "User with the specified ID not found",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))
      ),
      @ApiResponse(
          responseCode = "500",
          description = "Internal Server Error",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))
      )
  })
  ApiSuccessResponse<User> getUserByID(@Valid @PathVariable String id) {
    return new ApiSuccessResponse<>(String.format("user with id %s", id), HttpStatus.OK,
        userService.getUserById(id));
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "Register new User")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201",
          description = "User Registered",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiSuccessResponse.class))
      ),
      @ApiResponse(
          responseCode = "400",
          description = "Request Validation failures",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))),
      @ApiResponse(
          responseCode = "409",
          description = "Another user with similar email/mobile/phone already exists",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))
      ),
      @ApiResponse(
          responseCode = "500",
          description = "Internal Server Error",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))
      ),
  })
  ApiSuccessResponse<User> createUser(@Valid @RequestBody CreateUserRequest reqData) {
    return new ApiSuccessResponse<>("user registration successful", HttpStatus.CREATED,
        userService.createUser(reqData));
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update User Details")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "User Updated",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiSuccessResponse.class))
      ),
      @ApiResponse(
          responseCode = "400",
          description = "Request Validation failures",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))),
      @ApiResponse(
          responseCode = "409",
          description = "Another user with similar mobile/phone already exists",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))
      ),
      @ApiResponse(
          responseCode = "500",
          description = "Internal Server Error",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))
      ),
  })
  ApiSuccessResponse<User> updateUser(@PathVariable String id,
      @Valid @RequestBody UpdateUserRequest reqData) {
    return new ApiSuccessResponse<>("user details updated", HttpStatus.CREATED,
        userService.updateUser(id, reqData));
  }

  @PatchMapping("/change-role")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize("hasAuthority('ADMIN')")
  @SecurityRequirement(name = "bearerAuth")
  @Operation(summary = "Change User Role")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "User Role Updated"),
      @ApiResponse(
          responseCode = "401",
          description = "Bearer Token missing or has expired",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))
      ),
      @ApiResponse(
          responseCode = "403",
          description = "Insufficient Privilege to perform account",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))
      ),
      @ApiResponse(
          responseCode = "404",
          description = "User with the specified ID not found",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))
      ),
      @ApiResponse(
          responseCode = "409",
          description = "User already has the specified role",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))
      ),
      @ApiResponse(
          responseCode = "500",
          description = "Internal Server Error",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))
      )
  })
  void modifyUserRole(@RequestParam(name = "id") @NotBlank @Size(min = 36, max = 36) String id,
      @RequestParam(name = "role") Role role) {
    userService.updateUserRole(id, role);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @SecurityRequirement(name = "bearerAuth")
  @Operation(summary = "Delete User")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "User Updated"),
      @ApiResponse(
          responseCode = "401",
          description = "Bearer Token missing or has expired",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))
      ),
      @ApiResponse(
          responseCode = "403",
          description = "Insufficient Privilege to perform account",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))
      ),
      @ApiResponse(
          responseCode = "404",
          description = "User with the specified ID not found",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))
      ),
      @ApiResponse(
          responseCode = "500",
          description = "Internal Server Error",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))
      )
  })
  void deleteUser(@PathVariable String id, Authentication auth) {
    userService.deleteUser(id, (User) auth.getPrincipal());
  }

}
