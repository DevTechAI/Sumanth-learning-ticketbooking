package com.arjuncodes.springemaildemo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class sendMailWithAttachment {

    @Value("${brevo.api.key}")
    private String brevoApiKey;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void setDataNsendMail(
            List<String> toEmails,
            String fromEmail,
            String bccEmail,
            String message) throws Exception
    {

        List<Map<String, String>> toList = new ArrayList<>();

        for (String email : toEmails) {
            Map<String, String> recipient = new HashMap<>();
            recipient.put("email", email);
            toList.add(recipient);
        }

        Map<String, Object> sender = new HashMap<>();
        sender.put("email", fromEmail);
        sender.put("name", "Spring Mail App");

        Map<String, Object> requestBody = new HashMap<>();

        requestBody.put("sender", sender);
        requestBody.put("to", toList);

        // Since your UI does not need subject/message,
        // keep these predefined here.
        requestBody.put("subject", "Email from Spring Mail App");

        requestBody.put(
                "htmlContent",
                "<html><body><p>" + message + "</p></body></html>"
        );

        if (bccEmail != null && !bccEmail.isBlank()) {

            List<Map<String, String>> bccList = new ArrayList<>();

            Map<String, String> bcc = new HashMap<>();
            bcc.put("email", bccEmail);

            bccList.add(bcc);

            requestBody.put("bcc", bccList);
        }

        String jsonBody = objectMapper.writeValueAsString(requestBody);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.brevo.com/v3/smtp/email"))
                .header("accept", "application/json")
                .header("api-key", brevoApiKey)
                .header("content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() < 200 ||
                response.statusCode() >= 300) {

            throw new RuntimeException(
                    "Brevo email failed. Status: "
                            + response.statusCode()
                            + " Response: "
                            + response.body()
            );
        }

        System.out.println(
                "Email sent successfully through Brevo: "
                        + response.body()
        );
    }
}