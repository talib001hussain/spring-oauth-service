package com.authentication.auth.controller;

import com.authentication.auth.dto.UserProfileResponse;
import com.authentication.auth.model.User;
import com.authentication.auth.model.UserProfile;
import com.authentication.auth.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        UserProfile userProfile = userService.getUserProfileById(user.getId())
                .orElseThrow(() -> new RuntimeException("User profile not found"));
        
        UserProfileResponse response = UserProfileResponse.builder()
                .userId(user.getId())
                .name(userProfile.getName())
                .height(userProfile.getHeight())
                .bmi(userProfile.getBmi())
                .build();
        
        return ResponseEntity.ok(response);
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateUserProfile(@RequestBody UserProfileResponse request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        UserProfile updatedProfile = userService.updateUserProfile(
                user.getId(),
                request.getName(),
                request.getHeight(),
                request.getBmi()
        );
        
        UserProfileResponse response = UserProfileResponse.builder()
                .userId(user.getId())
                .name(updatedProfile.getName())
                .height(updatedProfile.getHeight())
                .bmi(updatedProfile.getBmi())
                .build();
        
        return ResponseEntity.ok(response);
    }
}