package com.emailwork.basic.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private final String configuredFromAddress;
    private final String smtpUsername;

    public EmailService(
            JavaMailSender mailSender,
            @Value("${app.mail.from:}") String configuredFromAddress,
            @Value("${spring.mail.username:}") String smtpUsername
    ) {
        this.mailSender = mailSender;
        this.configuredFromAddress = configuredFromAddress;
        this.smtpUsername = smtpUsername;
    }

    public void sendEmail(
            String to,
            String subject,
            String body,
            List<String> cc,
            List<MultipartFile> attachments
    ) {
        validateRequired("to", to);
        validateRequired("subject", subject);
        validateRequired("body", body);

        try {
            List<MultipartFile> safeAttachments = attachments == null ? List.of() : attachments;
            boolean hasAttachments = safeAttachments.stream()
                    .filter(Objects::nonNull)
                    .anyMatch(file -> !file.isEmpty());

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, hasAttachments);

            helper.setFrom(fromAddress());
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, false);

            List<String> safeCc = cc == null ? List.of() : cc;
            for (String ccAddress : safeCc) {
                if (StringUtils.hasText(ccAddress)) {
                    helper.addCc(ccAddress.trim());
                }
            }

            for (MultipartFile file : safeAttachments) {
                if (file != null && !file.isEmpty()) {
                    String filename = StringUtils.hasText(file.getOriginalFilename())
                            ? file.getOriginalFilename()
                            : "attachment";
                    helper.addAttachment(filename, file);
                }
            }

            mailSender.send(message);
        } catch (MessagingException | MailException exception) {
            throw new IllegalStateException("Failed to send email", exception);
        }
    }

    private void validateRequired(String fieldName, String value) {
        if (!StringUtils.hasText(value)) {
            throw new IllegalArgumentException(fieldName + " is required");
        }
    }

    private String fromAddress() {
        if (StringUtils.hasText(configuredFromAddress)) {
            return configuredFromAddress.trim();
        }
        if (StringUtils.hasText(smtpUsername)) {
            return smtpUsername.trim();
        }

        throw new IllegalStateException("Configure app.mail.from or spring.mail.username before sending email");
    }
}
