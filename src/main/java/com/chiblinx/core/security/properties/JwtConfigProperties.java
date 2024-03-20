package com.chiblinx.core.security.properties;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtConfigProperties {

  @NotNull
  private String publicKey;

  @NotNull
  private String privateKey;

}
