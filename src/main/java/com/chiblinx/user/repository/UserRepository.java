package com.chiblinx.user.repository;

import com.chiblinx.user.entity.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<User, UUID> {

  @Query(""" 
      select u from User u
      where (:searchTerm is null or upper(u.firstName) like upper(concat('%', :searchText, '%' )))
      or (:searchTerm is null or upper(u.surname) like upper(concat('%', :searchText, '%' )))
      or (:searchTerm is null or upper(u.phone) like upper(concat('%', :searchText, '%' )))
      or (:searchTerm is null or upper(u.mobile) like upper(concat('%', :searchText, '%' )))
      and (:startDate is null or u.createdAt >= :startDate)
      and (:endDate is null or u.createdAt <= :endDate)
      order by u.createdAt DESC
      """)
  List<User> findByOrderByCreatedAtDesc(
      @Param("searchTerm") String searchTerm,
      @Param("startDate") LocalDateTime startDate,
      @Param("startDate") LocalDateTime endDate,
      Pageable pageable);

  Optional<User> findByEmail(String email);

  @Modifying
  @Query("update User u set u.lastLogin = :lastLogin, u.refreshToken = :refreshToken where u.email = :email")
  void updateLastLogin(@Param(value = "lastLogin") LocalDateTime lastLogin,
      @Param(value = "refreshToken") String refreshToken, @Param(value = "email") String email);


  @Modifying
  @Query("update User u set u.code = :code, u.codeExpiry = :codeExpiry where u.email = :email")
  void updateCode(@Param("code") String code, @Param("codeExpiry") Long codeExpiry,
      @Param("email") String email);

  @Modifying
  @Query("update User u set u.refreshToken = :refreshToken where u.email = :email")
  void invalidateRefreshToken(@Param(value = "refreshToken") String refreshToken,
      @Param(value = "email") String email);

  @Transactional
  @Modifying
  @Query("update User u set u.password = :password where u.id = :id")
  void updatePassword(@Param("password") String password, @Param("id") UUID id);


}
