package com.emailwork.basic.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.emailwork.basic.dto.EmailRequest;
import com.emailwork.basic.dto.EmailResponse;
import com.emailwork.basic.service.EmailService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/email")
public class EmailController {

    private final EmailService emailService;

    @PostMapping(value = "/send", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EmailResponse> sendEmail(@RequestBody EmailRequest request) {
        emailService.sendEmail(
                request.to(),
                request.subject(),
                request.body(),
                request.cc(),
                List.of()
        );

        return ResponseEntity.ok(new EmailResponse("Email sent successfully"));
    }

    @PostMapping(value = "/sendWith", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EmailResponse> sendEmailWithAttachments(
            @RequestParam String to,
            @RequestParam String subject,
            @RequestParam String body,
            @RequestParam(required = false) List<String> cc,
            @RequestParam(required = false) List<MultipartFile> attachments
    ) {
        emailService.sendEmail(to, subject, body, cc, attachments);

        return ResponseEntity.ok(new EmailResponse("Email sent successfully"));
    }
}
