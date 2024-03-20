package com.chiblinx.core.services.email;

import com.chiblinx.core.services.email.usecase.EmailDetails;
import com.chiblinx.core.services.email.usecase.MultipleEmailDetails;
import jakarta.mail.internet.MimeMessage;
import java.io.File;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

  private static final String FROM_ADDRESS = "noreply@tenanttide.com";
  private final JavaMailSender mailSender;
  private final TaskExecutor executor;

  @Override
  public void sendSimpleMail(EmailDetails details) {
    try {
      SimpleMailMessage mailMessage = new SimpleMailMessage();

      mailMessage.setFrom(FROM_ADDRESS);
      mailMessage.setTo(details.getRecipient());
      mailMessage.setText(details.getMsgBody());
      mailMessage.setSubject(details.getSubject());

      mailSender.send(mailMessage);

    } catch (Exception ex) {
      log.error("error sending email [{}]", ex.getLocalizedMessage());
      throw new RuntimeException(ex);
    }
  }

  @Override
  public void sendMailWithAttachment(EmailDetails details) {
    MimeMessage mimeMessage = mailSender.createMimeMessage();

    try {
      MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

      mimeMessageHelper.setFrom(FROM_ADDRESS);
      mimeMessageHelper.setTo(details.getRecipient());
      mimeMessageHelper.setText(details.getMsgBody());
      mimeMessageHelper.setSubject(details.getSubject());

      FileSystemResource file = new FileSystemResource(new File(details.getAttachment()));

      mimeMessageHelper.addAttachment(Objects.requireNonNull(file.getFilename()), file);

      mailSender.send(mimeMessage);

    } catch (Exception ex) {
      log.error("error sending email [{}]", ex.getLocalizedMessage());
      throw new RuntimeException(ex);
    }
  }

  @Override
  @Async("taskExecutorDefault")
  public void sendBulkSimpleMail(MultipleEmailDetails multipleEmailDetails) {
    for (String recipient : multipleEmailDetails.getRecipients()) {
      EmailDetails emailDetails = EmailDetails.builder()
          .recipient(recipient)
          .subject(multipleEmailDetails.getSubject())
          .msgBody(multipleEmailDetails.getMsgBody())
          .build();
      executor.execute(() -> sendSimpleMail(emailDetails));
    }
  }

  @Override
  @Async("taskExecutorDefault")
  public void sendBulkMailWithAttachment(MultipleEmailDetails multipleEmailDetails) {
    for (String recipient : multipleEmailDetails.getRecipients()) {
      EmailDetails emailDetails = EmailDetails.builder()
          .recipient(recipient)
          .subject(multipleEmailDetails.getSubject())
          .msgBody(multipleEmailDetails.getMsgBody())
          .attachment(multipleEmailDetails.getAttachment())
          .build();
      executor.execute(() -> sendMailWithAttachment(emailDetails));
    }
  }
}
