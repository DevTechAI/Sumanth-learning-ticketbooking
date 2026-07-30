package com.handson.Oauth.controller;

import com.handson.Oauth.dto.AppUserResponse;
import com.handson.Oauth.dto.CreateEmployeeRequest;
import com.handson.Oauth.service.AppUserService;
import com.handson.Oauth.util.ResponseHeaderBuilder;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AppUserService appUserService;

    public AdminController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<AppUserResponse>> users() {
        List<AppUserResponse> users = appUserService.findAllUsers()
                .stream()
                .map(AppUserResponse::from)
                .toList();

        return ResponseEntity.ok()
                .headers(ResponseHeaderBuilder.jsonHeaders("admin-users"))
                .body(users);
    }

    @PostMapping("/employees")
    public ResponseEntity<AppUserResponse> createEmployee(@Valid @RequestBody CreateEmployeeRequest request) {
        AppUserResponse response = AppUserResponse.from(appUserService.createEmployee(
                request.username(),
                request.displayName(),
                request.email()
        ));

        return ResponseEntity.ok()
                .headers(ResponseHeaderBuilder.jsonHeaders("admin-create-employee"))
                .body(response);
    }
}
