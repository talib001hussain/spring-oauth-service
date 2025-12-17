package com.authentication.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {

    @GetMapping("/login")
    public ResponseEntity<?> login() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Please use OAuth2 providers to login");
        response.put("oauth2_providers", new String[]{"github", "google"});
        return ResponseEntity.ok(response);
    }

    @GetMapping("/")
    public ResponseEntity<?> home() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Welcome to the Authentication Service");
        response.put("login_url", "/login");
        return ResponseEntity.ok(response);
    }
}
