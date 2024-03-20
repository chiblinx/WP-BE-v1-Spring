package com.chiblinx.core.services.email;

import com.chiblinx.core.services.email.usecase.EmailDetails;
import com.chiblinx.core.services.email.usecase.MultipleEmailDetails;

public interface EmailService {

  void sendSimpleMail(EmailDetails details);

  void sendMailWithAttachment(EmailDetails details);

  void sendBulkSimpleMail(MultipleEmailDetails multipleEmailDetails);

  void sendBulkMailWithAttachment(MultipleEmailDetails multipleEmailDetails);

}
