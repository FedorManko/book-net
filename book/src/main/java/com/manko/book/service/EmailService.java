package com.manko.book.service;

import com.manko.book.enums.EmailTemplateName;
import jakarta.mail.MessagingException;

public interface EmailService {

  void sendEmail(String to, String username, EmailTemplateName emailTemplate,
      String confirmationUrl, String activationCode, String subject) throws MessagingException;

}
