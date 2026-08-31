package com.Note_Vault.controller;

import com.Note_Vault.entity.Note;
import com.Note_Vault.service.NoteService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController {
    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping
    public Note createNote(@RequestBody Note note){
        return noteService.createNote(note);
    }

    @GetMapping
    public List<Note> getAllNotes(){
        return noteService.getAllNotes();
    }

    @GetMapping("/{id}")
    public  Note getNotesbyId(@PathVariable Long id){
        return noteService.getNotesById(id);
    }
    @PutMapping("/{id}")
    public Note updateNote(@PathVariable Long id, @RequestBody Note note){
        return noteService.updateNote(id,note);
    }
    @DeleteMapping("/{id}")
    public void deleteNoteById(@PathVariable Long id){
         noteService.deleteNoteById(id);
    }
}
