package com.authentication.auth.controller;

import com.authentication.auth.dto.JwtResponse;
import com.authentication.auth.dto.SigninRequest;
import com.authentication.auth.dto.SignupRequest;
import com.authentication.auth.model.User;
import com.authentication.auth.security.JwtTokenUtil;
import com.authentication.auth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtTokenUtil jwtTokenUtil;

    public AuthController(AuthService authService, JwtTokenUtil jwtTokenUtil) {
        this.authService = authService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest) {
        User user = authService.signup(signupRequest.getEmail(), signupRequest.getPassword());
        return ResponseEntity.ok("User registered successfully with email: " + user.getEmail());
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody SigninRequest signinRequest) {
        Authentication authentication = authService.signin(signinRequest.getEmail(), signinRequest.getPassword());
        String email = authentication.getName();
        String token = jwtTokenUtil.generateToken(email);

        JwtResponse response = JwtResponse.builder()
                .token(token)
                .type("Bearer")
                .email(email)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/user")
    public ResponseEntity<?> getCurrentUser() {
        User user = authService.getCurrentUser();
        if (user == null) {
            return ResponseEntity.status(401).body("User not authenticated");
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Logged out successfully");
    }
}
