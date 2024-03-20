package com.chiblinx.core.config;

import com.chiblinx.core.properties.CorsConfigProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class CorsConfiguration {

  private final CorsConfigProperties corsConfigProps;

  @Bean
  WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        WebMvcConfigurer.super.addCorsMappings(registry);
        registry.addMapping("/**")
            .allowedMethods("OPTIONS", "GET", "PUT", "POST", "PATCH", "DELETE")
            .allowedOriginPatterns(corsConfigProps.getOriginPatterns())
            .allowCredentials(true);
      }
    };
  }

}
