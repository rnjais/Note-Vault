package com.Note_Vault.service;
import com.Note_Vault.entity.Note;
import com.Note_Vault.exception.NoteNotFoundException;
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
        return noteRepository.findById(id).orElseThrow(() ->
                                            new NoteNotFoundException("Note with id " +id+ " not found"));
    }

    public Note updateNote(Long id, Note note) {

        // If note exists, store it in existingNote.
        // Otherwise, throw NoteNotFoundException.
        Note existingNote = noteRepository.findById(id)
                .orElseThrow(() ->
                        new NoteNotFoundException("Note with id " + id + " not found"));

        // Update the title
        existingNote.setTitle(note.getTitle());

        // Update the content
        existingNote.setContent(note.getContent());

        // Save the updated note to the database
        return noteRepository.save(existingNote);
    }

    // Delete a note by its ID
    public void deleteNoteById(Long id) {

        // Check whether the note exists
        if (!noteRepository.existsById(id)) {
            throw new NoteNotFoundException(
                    "Note with id " + id + " not found"
            );
        }

        // Delete the note
        noteRepository.deleteById(id);
    }
}