package com.arjuncodes.springemaildemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/emails")
public class EmailController {

    @Autowired
    private sendMailWithAttachment sendMailWithAttachment;

    @PostMapping("/send")
    public ResponseEntity<String> sendEmails(
            @RequestBody EmailRequest emailRequest
    ) throws Exception {

        if (emailRequest.getToEmails() == null ||
                emailRequest.getToEmails().isEmpty()) {

            return ResponseEntity
                    .badRequest()
                    .body("toEmails is required");
        }

        if (emailRequest.getFromEmail() == null ||
                emailRequest.getFromEmail().isBlank()) {

            return ResponseEntity
                    .badRequest()
                    .body("fromEmail is required");
        }

        sendMailWithAttachment.setDataNsendMail(
                emailRequest.getToEmails(),
                emailRequest.getFromEmail(),
                emailRequest.getBccEmail(),
                emailRequest.getMessage()
        );

        return ResponseEntity.ok(
                "Emails sent successfully"
        );
    }
}