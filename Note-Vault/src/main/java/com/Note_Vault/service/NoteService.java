package com.Note_Vault.service;

import com.Note_Vault.entity.Note;
import com.Note_Vault.exception.NoteNotFoundException;
import com.Note_Vault.mapper.NoteMapper;
import com.Note_Vault.repository.NoteRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import com.Note_Vault.dto.NoteDTO;

@Service
public class NoteService {

    private final NoteRepository noteRepository;

    // Constructor injection for NoteRepository
    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    // Create a new note
    public Note createNote(NoteDTO noteDTO) {

        Note note = NoteMapper.toEntity(noteDTO);
        note.setCreatedAt(LocalDateTime.now());

        return noteRepository.save(note);
    }

    // Get all notes from the database
    public Page<NoteDTO> getAllNotes(int page, int size, String sortBy, String direction) {

        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Note> notes = noteRepository.findAll(pageable);

//        Think like:-
//        List<Note>
//    ↓
//        Only notes
//
//        Page<Note>
//    ↓
//        Notes + pagination information

        return notes.map(note -> {
            NoteDTO noteDTO = NoteMapper.toDTO(note);
            return noteDTO;
        });

    }

    // Get a single note by its ID
    public NoteDTO getNoteById(Long id) {
        Note note = noteRepository.findById(id).orElseThrow(() ->
                new NoteNotFoundException("Note with id " + id + " not found"));
         NoteDTO noteDTO = NoteMapper.toDTO(note);
        return noteDTO;
    }

    public Note updateNote(Long id, NoteDTO noteDTO) {

        // If note exists, store it in existingNote.
        // Otherwise, throw NoteNotFoundException.
        Note existingNote = noteRepository.findById(id)
                .orElseThrow(() ->
                        new NoteNotFoundException("Note with id " + id + " not found"));

        // Update the title
        existingNote.setTitle(noteDTO.getTitle());

        // Update the content
        existingNote.setContent(noteDTO.getContent());

        //Update Category
        existingNote.setCategory(noteDTO.getCategory());
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

    //Search By Keyword (title/content)
    public List<NoteDTO> searchNotesByTitle(String keyword) {
        List<Note> notes = noteRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(keyword, keyword);
        return notes.stream()
                .map(note -> {
                    NoteDTO noteDTO = NoteMapper.toDTO(note);

                    return noteDTO;
                })
                .toList();

    }

    //Search By Category
    public List<NoteDTO> searchNotesByCategory(String category) {
        List<Note> notes = noteRepository.findByCategoryContainingIgnoreCase(category);
        return notes.stream()
                .map(note -> {
                    NoteDTO noteDTO = NoteMapper.toDTO(note);

                    return noteDTO;
                })
                .toList();
    }

}