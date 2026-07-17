package com.Demo.JWT.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TestController
{

    @GetMapping("/public-test")
    public String publicTest() {
        return "This is public test API";
    }

    @GetMapping("/secure")
    public String secureApi() {
        return "You are authenticated. JWT is valid.";
    }


}
