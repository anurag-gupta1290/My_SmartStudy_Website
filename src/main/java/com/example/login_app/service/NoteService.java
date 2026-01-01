package com.example.login_app.service;

import com.example.login_app.entity.Note;
import com.example.login_app.entity.User;
import com.example.login_app.repository.NoteRepository;
import com.example.login_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private UserRepository userRepository;

    // Frontend (loadNotes) ke liye
    public List<Note> getNotesByUserId(Long userId) {
        return noteRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    // Frontend (createNote) ke liye
    public Note saveNoteWithUserId(String title, String content, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Note note = new Note();
        note.setTitle(title);
        note.setContent(content);
        note.setUser(user); // SQL table mein user_id column isi se bharega
        return noteRepository.save(note);
    }

    public void deleteNoteSecurely(Long id, Long userId) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found"));
        if (!note.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized delete attempt!");
        }
        noteRepository.deleteById(id);
    }
}