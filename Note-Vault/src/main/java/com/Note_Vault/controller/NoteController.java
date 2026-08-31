package com.Note_Vault.controller;

import jakarta.validation.Valid;
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

    // Inject NoteService using constructor injection
    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    // Create a new note
    @PostMapping
    public ResponseEntity<Note> createNote(@Valid @RequestBody Note note) {

        // Pass the note to the service and save it in the database
        Note savedNote = noteService.createNote(note);

        // Return the created note with HTTP status 201 CREATED
        return ResponseEntity.status(HttpStatus.CREATED).body(savedNote);
    }

    // Get all notes
    @GetMapping
    public List<Note> getAllNotes() {

        // Fetch all notes from the database through the service
        return noteService.getAllNotes();
    }

    // Get a single note by its ID
    @GetMapping("/{id}")
    public ResponseEntity<Note> getNoteById(@PathVariable Long id) {

        // Find the note by ID; throws exception if the note does not exist
        Note note = noteService.getNoteById(id);

        // Return the note with HTTP status 200 OK
        return ResponseEntity.ok(note);
    }

    // Update an existing note
    @PutMapping("/{id}")
    public ResponseEntity<Note> updateNote(
            @PathVariable Long id,
            @Valid @RequestBody Note note) {

        // Find the existing note and update its details
        Note updatedNote = noteService.updateNote(id, note);

        // Return the updated note with HTTP status 200 OK
        return ResponseEntity.ok(updatedNote);
    }

    // Delete a note by its ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNoteById(@PathVariable Long id) {

        // Delete the note; throws exception if the note does not exist
        noteService.deleteNoteById(id);

        // Return a success message with HTTP status 200 OK
        return ResponseEntity.ok("Note deleted successfully");
    }
}