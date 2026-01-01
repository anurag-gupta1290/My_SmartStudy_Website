package com.example.login_app.repository;

import com.example.login_app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Must declare these methods so your service can use them
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
}
