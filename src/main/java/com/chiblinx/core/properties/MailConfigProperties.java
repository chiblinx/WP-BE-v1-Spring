package com.chiblinx.core.properties;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "mail")
public class MailConfigProperties {

  @NotNull
  private String host;

  @NotNull
  private Integer port;

  @NotNull
  private String username;

  @NotNull
  private String password;

}
