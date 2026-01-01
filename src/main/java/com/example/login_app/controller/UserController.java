package com.example.login_app.controller;

import org.springframework.security.core.Authentication; // ✅ missing import
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @GetMapping("/profile")
    public Map<String, String> getUserProfile(Authentication auth) {
        // current logged-in user
        String username = auth.getName();
        return Map.of("username", username);
    }
}
