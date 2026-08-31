package com.Note_Vault.service;

import com.Note_Vault.entity.Note;
import com.Note_Vault.repository.NoteRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NoteService {

    private final NoteRepository noteRepository;

    // Constructor injection for NoteRepository
    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    // Create a new note
    public Note createNote(Note note) {

        // Set the current date and time when the note is created
        note.setCreatedAt(LocalDateTime.now());

        // Save the note to the database
        return noteRepository.save(note);
    }

    // Get all notes from the database
    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    // Get a single note by its ID
    public Note getNoteById(Long id) {

        // Find the note using its ID
        // Return null if the note does not exist
        return noteRepository.findById(id).orElse(null);
    }

    // Update an existing note
    public Note updateNote(Long id, Note note) {

        // Find the existing note in the database
        Note existingNote = noteRepository.findById(id).orElse(null);

        // If the note doesn't exist, return null
        if (existingNote == null) {
            return null;
        }

        // Update the title with the new title
        existingNote.setTitle(note.getTitle());

        // Update the content with the new content
        existingNote.setContent(note.getContent());

        // Save the updated note to the database
        return noteRepository.save(existingNote);
    }

    // Delete a note by its ID
    public boolean deleteNoteById(Long id) {

        // Check whether the note exists
        if (!noteRepository.existsById(id)) {
            return false;
        }

        // Delete the note from the database
        noteRepository.deleteById(id);

        // Return true when deletion is successful
        return true;
    }
}