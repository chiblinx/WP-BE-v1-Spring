package com.chiblinx.core.security;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

  public static final String[] WHITELIST_ENDPOINTS = {
      "/",
      "/swagger-ui/**",
      "/v3/api-docs/**",
      "/v1/auth/login",
      "/v1/auth/refresh-token",
      "/v1/auth/code",
      "/v1/auth/reset-password",
      "/management/**"
  };

  private static final String[] WHITELIST_POST_ENDPOINTS = {
      "/v1/users",
  };
  private final AuthenticationProvider authenticationProvider;
  private final JwtAuthenticationFilter jwtAuthenticationFilter;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .cors(AbstractHttpConfigurer::disable)
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            authorize -> authorize
                .requestMatchers(HttpMethod.POST, WHITELIST_POST_ENDPOINTS)
                .permitAll()
                .requestMatchers(WHITELIST_ENDPOINTS)
                .permitAll()
                .anyRequest()
                .authenticated()
        )
        .exceptionHandling(handlingConfigurer -> handlingConfigurer.authenticationEntryPoint(
            (request, response, authException) -> response.sendError(
                HttpServletResponse.SC_UNAUTHORIZED)))
        .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authenticationProvider(authenticationProvider)
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
