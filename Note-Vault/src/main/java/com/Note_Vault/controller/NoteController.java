package com.Note_Vault.controller;

import com.Note_Vault.entity.Note;
import com.Note_Vault.service.NoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteService noteService;

    // Constructor injection
    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    // Create a new note
    @PostMapping
    public ResponseEntity<Note> createNote(@RequestBody Note note) {

        // Save the note using the service
        Note savedNote = noteService.createNote(note);

        // Return 201 CREATED with the saved note
        return ResponseEntity.status(HttpStatus.CREATED).body(savedNote);
    }

    // Get all notes
    @GetMapping
    public List<Note> getAllNotes() {
        return noteService.getAllNotes();
    }

    // Get a single note by ID
    @GetMapping("/{id}")
    public ResponseEntity<Note> getNotebyId(@PathVariable Long id) {

        // Find note by ID
        Note note = noteService.getNoteById(id);

        // If note doesn't exist, return 404 NOT FOUND
        if (note == null) {
            return ResponseEntity.notFound().build();
        }

        // If note exists, return 200 OK with the note
        return ResponseEntity.ok(note);
    }

    // Update an existing note
    @PutMapping("/{id}")
    public ResponseEntity<Note> updateNote(
            @PathVariable Long id,
            @RequestBody Note note) {

        // Update the note
        Note updatedNote = noteService.updateNote(id, note);

        // If note doesn't exist, return 404 NOT FOUND
        if (updatedNote == null) {
            return ResponseEntity.notFound().build();
        }

        // If update is successful, return 200 OK
        return ResponseEntity.ok(updatedNote);
    }

    // Delete a note by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNoteById(@PathVariable Long id) {

        // Try to delete the note
        boolean deleted = noteService.deleteNoteById(id);

        // If note doesn't exist, return 404 NOT FOUND
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        // If deletion is successful, return 200 OK
        return ResponseEntity.ok("Note deleted successfully");
    }
}