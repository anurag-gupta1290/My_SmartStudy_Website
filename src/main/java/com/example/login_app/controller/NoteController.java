package com.example.login_app.controller;

import com.example.login_app.entity.Note;
import com.example.login_app.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    @Autowired
    private NoteService noteService;

    // GET: /api/notes/user/1
    @GetMapping("/user/{userId}")
    public List<Note> getUserNotes(@PathVariable Long userId) {
        return noteService.getNotesByUserId(userId);
    }

    // POST: /api/notes
    @PostMapping
    public Note createNote(@RequestBody Map<String, Object> payload) {
        String title = (String) payload.get("title");
        String content = (String) payload.get("content");
        Long userId = Long.valueOf(payload.get("userId").toString());

        return noteService.saveNoteWithUserId(title, content, userId);
    }

    // DELETE: /api/notes/101?userId=1
    @DeleteMapping("/{id}")
    public void deleteNote(@PathVariable Long id, @RequestParam Long userId) {
        noteService.deleteNoteSecurely(id, userId);
    }
}