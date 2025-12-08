package com.authentication.auth.controller;

import com.authentication.auth.dto.JwtResponse;
import com.authentication.auth.dto.SigninRequest;
import com.authentication.auth.dto.SignupRequest;
import com.authentication.auth.model.User;
import com.authentication.auth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest) {
        User user = authService.signup(signupRequest.getEmail(), signupRequest.getPassword());
        return ResponseEntity.ok("User registered successfully with email: " + user.getEmail());
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody SigninRequest signinRequest) {
        String jwt = authService.signin(signinRequest.getEmail(), signinRequest.getPassword());
        return ResponseEntity.ok(new JwtResponse(jwt, signinRequest.getEmail()));
    }
}