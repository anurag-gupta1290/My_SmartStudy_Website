package com.example.login_app.repository;

import com.example.login_app.entity.Note;
import com.example.login_app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    // "User" entity ka naam hai aur "Id" uski primary key hai
    List<Note> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<Note> findByUserId(Long userId);
    List<Note> findByUser(User user);
}
