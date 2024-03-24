package com.chiblinx.user.service;

import com.chiblinx.core.exceptions.ConflictException;
import com.chiblinx.core.exceptions.ForbiddenException;
import com.chiblinx.core.exceptions.NotFoundException;
import com.chiblinx.core.services.email.EmailService;
import com.chiblinx.core.services.email.usecase.EmailDetails;
import com.chiblinx.user.entity.User;
import com.chiblinx.user.entity.UserRole;
import com.chiblinx.user.enums.Role;
import com.chiblinx.user.repository.UserRepository;
import com.chiblinx.user.repository.UserRoleRepository;
import com.chiblinx.user.usecase.BasicUserInfo;
import com.chiblinx.user.usecase.CreateUserRequest;
import com.chiblinx.user.usecase.UpdateUserRequest;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final UserRoleRepository userRoleRepository;
  private final EmailService emailService;
  private final PasswordEncoder passwordEncoder;

  @Override
  public Page<BasicUserInfo> getAllUsers(String searchTerm, LocalDateTime startDate,
      LocalDateTime endDate, int page, int size) {
    final Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
    return userRepository.findAllWithFilters(searchTerm, startDate, endDate, pageable);
  }

  @Override
  public User getUserByEmail(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(
            () -> new NotFoundException(String.format("user with email: %s not found", email)));
  }

  @Override
  public User getUserById(String id) {
    return userRepository.findById(UUID.fromString(id))
        .orElseThrow(() -> new NotFoundException(String.format("user with id: %s not found", id)));
  }

  @Override
  public User createUser(CreateUserRequest reqData) {
    User newUser = User.builder()
        .email(reqData.getEmail())
        .password(passwordEncoder.encode(reqData.getPassword()))
        .firstName(reqData.getFirstName())
        .surname(reqData.getSurname())
        .mobile(reqData.getMobile())
        .dateOfBirth(reqData.getDateOfBirth())
        .role(reqData.getRole() != null ? getUserRole(Role.valueOf(reqData.getRole()))
            : getUserRole(Role.USER))
        .build();

    for (final Field field : reqData.getClass().getDeclaredFields()) {
      field.setAccessible(true);

      try {
        Object value = field.get(reqData);
        if (value != null) {
          switch (field.getName()) {
            case "phone":
              newUser.setPhone((String) value);
              break;
            case "aboutMe":
              newUser.setAboutMe((String) value);
              break;
            case "profilePhoto":
              newUser.setProfilePhoto((String) value);
              break;
            case "facebook":
              newUser.setFacebook((String) value);
              break;
            case "twitter":
              newUser.setTwitter((String) value);
              break;
            case "instagram":
              newUser.setInstagram((String) value);
              break;
            case "linkedin":
              newUser.setLinkedin((String) value);
              break;
            case "website":
              newUser.setWebsite((String) value);
              break;
          }
        }
      } catch (IllegalAccessException ex) {
        log.error("error setting lease update fields [{}]", ex.getLocalizedMessage());
        throw new RuntimeException(ex);
      }
    }

    User user = userRepository.save(newUser);

    String msgBody = String.format(
        "Dear %s, Thank you for using the property market place platform "
            + "Your account has been created", reqData.getFirstName());

    EmailDetails emailDetails = EmailDetails.builder()
        .recipient(reqData.getEmail())
        .msgBody(msgBody)
        .subject("VERIFICATION CODE")
        .build();

    emailService.sendSimpleMail(emailDetails);

    return user;
  }

  @Override
  public void updateUserRole(String id, Role role) {
    final User user = getUserById(id);

    if (user.getRole().getRoleName().name().equals(role.name())) {
      log.error("user with id: [{}] already has role: [{}]", id, role.name());
      throw new ConflictException("user already has the same role");
    }

    final UserRole userRole = getUserRole(role);
    userRepository.updateRoleById(userRole, user.getId());

    log.info("user role updated");
  }

  @Override
  public User updateUser(String id, UpdateUserRequest reqData) {
    final User userToUpdate = getUserById(id);

    for (final Field field : reqData.getClass().getDeclaredFields()) {
      field.setAccessible(true);

      try {
        Object value = field.get(reqData);
        if (value != null) {
          switch (field.getName()) {
            case "firstName":
              userToUpdate.setFirstName((String) value);
              break;
            case "surname":
              userToUpdate.setSurname((String) value);
              break;
            case "mobile":
              userToUpdate.setMobile((String) value);
              break;
            case "phone":
              userToUpdate.setPhone((String) value);
              break;
            case "dateOfBirth":
              userToUpdate.setDateOfBirth((LocalDate) value);
              break;
            case "aboutMe":
              userToUpdate.setAboutMe((String) value);
              break;
            case "profilePhoto":
              userToUpdate.setProfilePhoto((String) value);
              break;
            case "facebook":
              userToUpdate.setFacebook((String) value);
              break;
            case "twitter":
              userToUpdate.setTwitter((String) value);
              break;
            case "instagram":
              userToUpdate.setInstagram((String) value);
              break;
            case "linkedin":
              userToUpdate.setLinkedin((String) value);
              break;
            case "website":
              userToUpdate.setWebsite((String) value);
              break;
          }
        }
      } catch (IllegalAccessException ex) {
        log.error("error setting lease update fields [{}]", ex.getLocalizedMessage());
        throw new RuntimeException(ex);
      }
    }

    final User updatedUser = userRepository.save(userToUpdate);

    log.info("user with id: [{}] updated successfully", id);

    return updatedUser;
  }

  @Override
  public void deleteUser(String id, User user) {
    final User userToDelete = getUserById(id);

    if (userToDelete.getId() != user.getId()) {
      log.error("user account does not belong to user making request");
      throw new ForbiddenException("you not allowed to delete another user account");
    }

    userRepository.delete(userToDelete);

    log.info("user with id: [{}] deleted successfully", id);
  }

  private UserRole getUserRole(Role roleName) {
    return userRoleRepository.findByRoleName(roleName.name())
        .orElseThrow(
            () -> new NotFoundException(String.format("role with name: %s not found", roleName)));
  }
}
