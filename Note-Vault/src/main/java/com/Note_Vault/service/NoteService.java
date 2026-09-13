package com.Note_Vault.service;

import com.Note_Vault.dto.NoteDTO;
import com.Note_Vault.entity.Note;
import com.Note_Vault.entity.User;
import com.Note_Vault.exception.NoteNotFoundException;
import com.Note_Vault.exception.UserNotFoundException;
import com.Note_Vault.mapper.NoteMapper;
import com.Note_Vault.repository.NoteRepository;
import com.Note_Vault.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NoteService {

    private final NoteRepository noteRepository;
    private final NoteMapper noteMapper;
    private final UserRepository userRepository;

    public NoteService(
            NoteRepository noteRepository,
            NoteMapper noteMapper,
            UserRepository userRepository) {

        this.noteRepository = noteRepository;
        this.noteMapper = noteMapper;
        this.userRepository = userRepository;
    }

    // Create a new note
    public NoteDTO createNote(NoteDTO noteDTO) {

        User user = getLoggedInUser();

        Note note = noteMapper.toEntity(noteDTO);
        note.setCreatedAt(LocalDateTime.now());
        note.setUser(user);

        Note savedNote = noteRepository.save(note);

        return noteMapper.toDTO(savedNote);
    }

    // Get all notes
    public Page<NoteDTO> getAllNotes(
            int page,
            int size,
            String sortBy,
            String direction) {

        User user = getLoggedInUser();

        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Note> notes = noteRepository.findByUser(user, pageable);

        return notes.map(note -> noteMapper.toDTO(note));
    }

    // Get a single note by ID
    public NoteDTO getNoteById(Long id) {

        User user = getLoggedInUser();

        Note note = noteRepository.findById(id)
                .orElseThrow(() ->
                        new NoteNotFoundException(
                                "Note with id " + id + " not found"));

        checkNoteOwnership(note, user, id);

        return noteMapper.toDTO(note);
    }

    // Update a note
    public NoteDTO updateNote(Long id, NoteDTO noteDTO) {

        User user = getLoggedInUser();

        Note existingNote = noteRepository.findById(id)
                .orElseThrow(() ->
                        new NoteNotFoundException(
                                "Note with id " + id + " not found"));

        checkNoteOwnership(existingNote, user, id);

        existingNote.setTitle(noteDTO.getTitle());
        existingNote.setContent(noteDTO.getContent());
        existingNote.setCategory(noteDTO.getCategory());

        Note updatedNote = noteRepository.save(existingNote);

        return noteMapper.toDTO(updatedNote);
    }

    // Delete a note
    public void deleteNoteById(Long id) {

        User user = getLoggedInUser();

        Note note = noteRepository.findById(id)
                .orElseThrow(() ->
                        new NoteNotFoundException(
                                "Note with id " + id + " not found"));

        checkNoteOwnership(note, user, id);

        noteRepository.deleteById(id);
    }

    // Search by title or content
    public List<NoteDTO> searchNotesByTitle(String keyword) {

        User user = getLoggedInUser();

        List<Note> notes =
                noteRepository
                        .findByUserAndTitleContainingIgnoreCaseOrUserAndContentContainingIgnoreCase(
                                user,
                                keyword,
                                user,
                                keyword
                        );

        return notes.stream()
                .map(note -> noteMapper.toDTO(note))
                .toList();
    }

    // Search by category
    public List<NoteDTO> searchNotesByCategory(String category) {

        User user = getLoggedInUser();

        List<Note> notes =
                noteRepository
                        .findByUserAndCategoryContainingIgnoreCase(
                                user,
                                category
                        );

        return notes.stream()
                .map(note -> noteMapper.toDTO(note))
                .toList();
    }

    // Get the currently logged-in user
    private User getLoggedInUser() {

        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found"));
    }

    // Check whether the note belongs to the logged-in user
    private void checkNoteOwnership(
            Note note,
            User user,
            Long id) {

        if (!note.getUser().getId().equals(user.getId())) {
            throw new NoteNotFoundException(
                    "Note with id " + id + " not found");
        }
    }
}