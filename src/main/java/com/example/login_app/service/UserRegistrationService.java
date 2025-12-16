package com.example.login_app.service;

import com.example.login_app.entity.User;
import com.example.login_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserRegistrationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Register a new user - UPDATED for your form fields
    public User registerUser(String username, String email, String password) {
        // Create new user
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("USER");

        return userRepository.save(user);
    }

    // Find user by email
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Find user by username
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Check if email exists
    public boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    // Check if username exists
    public boolean usernameExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    // Get user by ID
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    // Update user profile
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    // Change password
    public boolean changePassword(Long userId, String newPassword) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return true;
        }
        return false;
    }
}