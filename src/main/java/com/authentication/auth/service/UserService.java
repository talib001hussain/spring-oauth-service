package com.authentication.auth.service;

import com.authentication.auth.model.User;
import com.authentication.auth.model.UserProfile;
import com.authentication.auth.repository.UserProfileRepository;
import com.authentication.auth.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserProfileRepository userProfileRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User createUser(String email, String password) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already in use");
        }

        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .build();

        User savedUser = userRepository.save(user);

        // Create an empty user profile
        UserProfile userProfile = UserProfile.builder()
                .user(savedUser)
                .build();
        userProfileRepository.save(userProfile);

        return savedUser;
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<UserProfile> getUserProfileById(UUID userId) {
        return userProfileRepository.findById(userId);
    }

    @Transactional
    public UserProfile updateUserProfile(UUID userId, String name, Double height, Double bmi) {
        UserProfile userProfile = userProfileRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User profile not found"));

        userProfile.setName(name);
        userProfile.setHeight(height);
        userProfile.setBmi(bmi);

        return userProfileRepository.save(userProfile);
    }
}