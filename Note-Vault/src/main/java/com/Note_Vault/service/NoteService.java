package com.Note_Vault.service;

import com.Note_Vault.entity.Note;
import com.Note_Vault.repository.NoteRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NoteService {
    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }
    public Note createNote(Note note){
        note.setCreatedAt(LocalDateTime.now());
        return noteRepository.save(note);
    }
    public List<Note> getAllNotes(){
        return noteRepository.findAll();
    }

    public Note getNotesById(Long id) {
        return noteRepository.findById(id).orElse(null);
    }

    public Note updateNote(Long id, Note note) {
        Note existingNote = noteRepository.findById(id).orElse(null);
        if(existingNote == null){
            return null;
        }
        existingNote.setTitle(note.getTitle());
        existingNote.setContent(note.getContent());
        return noteRepository.save(existingNote);
    }

    public void deleteNoteById(Long id) {
         noteRepository.deleteById(id);
    }
}
