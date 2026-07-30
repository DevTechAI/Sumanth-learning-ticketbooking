package com.handson.Oauth.controller;

import com.handson.Oauth.service.OauthTokenService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class SampleController {

    private final OauthTokenService oauthTokenService;

    public SampleController(OauthTokenService oauthTokenService) {
        this.oauthTokenService = oauthTokenService;
    }

    @GetMapping("/token")
    public String token() {
        return oauthTokenService.getAccessToken();
    }

}
