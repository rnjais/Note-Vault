package com.Note_Vault.service;

import com.Note_Vault.entity.User;
import com.Note_Vault.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final NoteMapper noteMapper;
    private final UserRepository userRepository;

    // Constructor injection for NoteRepository
    public NoteService(NoteRepository noteRepository, NoteMapper noteMapper, UserRepository userRepository) {
        this.noteRepository = noteRepository;
        this.noteMapper = noteMapper;
        this.userRepository = userRepository;
    }

    // Create a new note
    public NoteDTO createNote(NoteDTO noteDTO) {
        //Get the username of the currently logged-in user from Spring Security
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // Find the User object from the database using the username
        // If the user does not exist, throw an exception
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        Note note = noteMapper.toEntity(noteDTO);

        note.setCreatedAt(LocalDateTime.now());

        note.setUser(user);

        Note savedNote = noteRepository.save(note);

        return noteMapper.toDTO(savedNote);
    }

    // Get all notes from the database
    public Page<NoteDTO> getAllNotes(int page, int size, String sortBy, String direction) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("user not found"));

        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Note> notes = noteRepository.findByUser(user, pageable);

//        Think like:-
//        List<Note>
//    ↓
//        Only notes
//
//        Page<Note>
//    ↓
//        Notes + pagination information

        return notes.map(note -> {
            NoteDTO noteDTO = noteMapper.toDTO(note);
            return noteDTO;
        });

    }

    // Get a single note by its ID
    public NoteDTO getNoteById(Long id) {

        // Get the username of the currently logged-in user
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        // Find the User object from the database using the username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("user not found"));

        // Find the note using its ID
        Note note = noteRepository.findById(id)
                .orElseThrow(() ->
                        new NoteNotFoundException("Note with id " + id + " not found"));

        // Check whether this note belongs to the logged-in user
        if (!note.getUser().getId().equals(user.getId())) {
            throw new NoteNotFoundException(
                    "Note with id " + id + " not found"
            );
        }

        // Convert the Note entity into NoteDTO
        NoteDTO noteDTO = noteMapper.toDTO(note);

        // Return the note
        return noteDTO;
    }

    public NoteDTO updateNote(Long id, NoteDTO noteDTO) {

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
        Note updatedNote = noteRepository.save(existingNote);

        return noteMapper.toDTO(updatedNote);
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
                    NoteDTO noteDTO = noteMapper.toDTO(note);

                    return noteDTO;
                })
                .toList();

    }

    //Search By Category
    public List<NoteDTO> searchNotesByCategory(String category) {
        List<Note> notes = noteRepository.findByCategoryContainingIgnoreCase(category);
        return notes.stream()
                .map(note -> {
                    NoteDTO noteDTO = noteMapper.toDTO(note);

                    return noteDTO;
                })
                .toList();
    }

}