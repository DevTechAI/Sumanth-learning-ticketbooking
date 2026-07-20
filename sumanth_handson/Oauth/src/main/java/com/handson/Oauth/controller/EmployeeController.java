package com.handson.Oauth.controller;

import com.handson.Oauth.dto.AppUserResponse;
import com.handson.Oauth.service.AppUserService;
import com.handson.Oauth.util.ResponseHeaderBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

    private final AppUserService appUserService;

    public EmployeeController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping("/me")
    public ResponseEntity<?> profile(@AuthenticationPrincipal Jwt jwt)
    {
        String username = jwt.getSubject();

        return appUserService.findByUsername(username)
                .<ResponseEntity<?>>map(user -> ResponseEntity.ok()
                        .headers(ResponseHeaderBuilder.jsonHeaders("employee-profile"))
                        .body(AppUserResponse.from(user)))
                .orElseGet(() -> ResponseEntity.ok()
                        .headers(ResponseHeaderBuilder.jsonHeaders("employee-profile"))
                        .body(Map.of(
                                "username", username,
                                "message", "Authenticated employee has no local profile yet"
                        )));
    }
}
