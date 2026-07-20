package com.handson.Oauth.controller;

import com.handson.Oauth.util.ResponseHeaderBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/public")
public class PublicController {

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok()
                .headers(ResponseHeaderBuilder.jsonHeaders("public-health"))
                .body(Map.of(
                        "status", "UP",
                        "timestamp", Instant.now().toString()
                ));
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, String>> info() {
        return ResponseEntity.ok()
                .headers(ResponseHeaderBuilder.jsonHeaders("public-info"))
                .body(Map.of("message", "Public OAuth sample endpoint"));
    }
}
