package com.chiblinx.core.config;

import com.chiblinx.core.properties.MailConfigProperties;
import java.util.Properties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@RequiredArgsConstructor
public class EmailConfiguration {

  private final MailConfigProperties mailConfigProps;

  @Bean
  public JavaMailSender getJavaMailSender() {
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

    mailSender.setHost(mailConfigProps.getHost());
    mailSender.setPort(mailConfigProps.getPort());

    mailSender.setUsername(mailConfigProps.getUsername());
    mailSender.setPassword(mailConfigProps.getPassword());

    Properties props = mailSender.getJavaMailProperties();
    props.put("mail.transport.protocol", "smtp");
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.debug", "true");
    props.put("mail.smtp.timeout", 5000);
    props.put("mail.smtp.connectiontimeout", 5000);
    props.put("mail.smtp.writetimeout", 5000);

    return mailSender;
  }

}
