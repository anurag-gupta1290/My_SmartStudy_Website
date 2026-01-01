package com.example.login_app.service;

import com.example.login_app.entity.User;
import com.example.login_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {

        User user;

        // ✅ Email se login
        if (identifier.contains("@")) {
            user = userRepository.findByEmail(identifier)
                    .orElseThrow(() ->
                            new UsernameNotFoundException("User not found with email: " + identifier));
        }
        // ✅ Username se login
        else {
            user = userRepository.findByUsername(identifier)
                    .orElseThrow(() ->
                            new UsernameNotFoundException("User not found with username: " + identifier));
        }

        return buildUserDetails(user);
    }

    // ✅ Google OAuth support (optional but safe)
    public UserDetails loadUserByGoogleEmail(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Google user not found: " + email));

        return buildUserDetails(user);
    }

    // ✅ Common method (single source of truth)
    private UserDetails buildUserDetails(User user) {

        String role = user.getRole().startsWith("ROLE_")
                ? user.getRole()
                : "ROLE_" + user.getRole();

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),                 // Spring principal
                user.getPassword(),              // Encoded password
                Collections.singletonList(
                        new SimpleGrantedAuthority(role)
                )
        );
    }
}
