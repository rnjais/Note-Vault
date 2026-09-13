package com.Note_Vault;

import com.Note_Vault.dto.NoteDTO;
import com.Note_Vault.entity.Note;
import com.Note_Vault.entity.User;
import com.Note_Vault.exception.NoteNotFoundException;
import com.Note_Vault.mapper.NoteMapper;
import com.Note_Vault.repository.NoteRepository;
import com.Note_Vault.repository.UserRepository;
import com.Note_Vault.service.NoteService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class NoteServiceTest {

    private NoteRepository noteRepository;
    private NoteMapper noteMapper;
    private UserRepository userRepository;
    private NoteService noteService;

    private User user;
    private Note note;
    private NoteDTO noteDTO;

    @BeforeEach
    void setUp() {

        noteRepository = mock(NoteRepository.class);
        noteMapper = mock(NoteMapper.class);
        userRepository = mock(UserRepository.class);

        noteService = new NoteService(
                noteRepository,
                noteMapper,
                userRepository
        );

        user = new User(
                "aryan",
                "aryan@gmail.com",
                "encodedPassword"
        );

        user.setId(1L);

        note = new Note();
        note.setId(1L);
        note.setTitle("Java");
        note.setContent("Learning JUnit");
        note.setCategory("Programming");
        note.setUser(user);

        noteDTO = new NoteDTO();
        noteDTO.setId(1L);
        noteDTO.setTitle("Java");
        noteDTO.setContent("Learning JUnit");
        noteDTO.setCategory("Programming");

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "aryan",
                        null
                )
        );
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void createNote_shouldSaveNote() {

        when(userRepository.findByUsername("aryan"))
                .thenReturn(Optional.of(user));

        when(noteMapper.toEntity(noteDTO))
                .thenReturn(note);

        when(noteRepository.save(any(Note.class)))
                .thenReturn(note);

        when(noteMapper.toDTO(note))
                .thenReturn(noteDTO);

        NoteDTO result = noteService.createNote(noteDTO);

        assertEquals("Java", result.getTitle());
        assertEquals("Learning JUnit", result.getContent());
        assertEquals("Programming", result.getCategory());

        verify(userRepository).findByUsername("aryan");
        verify(noteMapper).toEntity(noteDTO);
        verify(noteRepository).save(any(Note.class));
        verify(noteMapper).toDTO(note);
    }

    @Test
    void getNoteById_shouldReturnNote_whenNoteBelongsToUser() {

        when(userRepository.findByUsername("aryan"))
                .thenReturn(Optional.of(user));

        when(noteRepository.findById(1L))
                .thenReturn(Optional.of(note));

        when(noteMapper.toDTO(note))
                .thenReturn(noteDTO);

        NoteDTO result = noteService.getNoteById(1L);

        assertEquals("Java", result.getTitle());
        assertEquals("Learning JUnit", result.getContent());
        assertEquals("Programming", result.getCategory());

        verify(userRepository).findByUsername("aryan");
        verify(noteRepository).findById(1L);
        verify(noteMapper).toDTO(note);
    }

    @Test
    void getNoteById_shouldThrowException_whenNoteDoesNotExist() {

        when(userRepository.findByUsername("aryan"))
                .thenReturn(Optional.of(user));

        when(noteRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                NoteNotFoundException.class,
                () -> noteService.getNoteById(99L)
        );

        verify(noteRepository).findById(99L);
        verify(noteMapper, never()).toDTO(any(Note.class));
    }

    @Test
    void getNoteById_shouldThrowException_whenNoteBelongsToAnotherUser() {

        User anotherUser = new User(
                "anotherUser",
                "another@gmail.com",
                "password"
        );

        anotherUser.setId(2L);

        note.setUser(anotherUser);

        when(userRepository.findByUsername("aryan"))
                .thenReturn(Optional.of(user));

        when(noteRepository.findById(1L))
                .thenReturn(Optional.of(note));

        assertThrows(
                NoteNotFoundException.class,
                () -> noteService.getNoteById(1L)
        );

        verify(noteMapper, never()).toDTO(any(Note.class));
    }

    @Test
    void updateNote_shouldUpdateNote_whenNoteBelongsToUser() {

        NoteDTO updatedDTO = new NoteDTO(
                1L,
                "Spring Boot",
                "Learning Spring Boot Testing",
                "Backend"
        );

        when(userRepository.findByUsername("aryan"))
                .thenReturn(Optional.of(user));

        when(noteRepository.findById(1L))
                .thenReturn(Optional.of(note));

        when(noteRepository.save(note))
                .thenReturn(note);

        when(noteMapper.toDTO(note))
                .thenReturn(updatedDTO);

        NoteDTO result = noteService.updateNote(1L, updatedDTO);

        assertEquals("Spring Boot", result.getTitle());
        assertEquals("Learning Spring Boot Testing", result.getContent());
        assertEquals("Backend", result.getCategory());

        verify(noteRepository).findById(1L);
        verify(noteRepository).save(note);
        verify(noteMapper).toDTO(note);
    }

    @Test
    void deleteNoteById_shouldDeleteNote_whenNoteBelongsToUser() {

        when(userRepository.findByUsername("aryan"))
                .thenReturn(Optional.of(user));

        when(noteRepository.findById(1L))
                .thenReturn(Optional.of(note));

        noteService.deleteNoteById(1L);

        verify(userRepository).findByUsername("aryan");
        verify(noteRepository).findById(1L);
        verify(noteRepository).deleteById(1L);
    }

    @Test
    void searchNotesByTitle_shouldReturnNotes() {

        when(userRepository.findByUsername("aryan"))
                .thenReturn(Optional.of(user));

        when(noteRepository
                .findByUserAndTitleContainingIgnoreCaseOrUserAndContentContainingIgnoreCase(
                        user,
                        "java",
                        user,
                        "java"
                ))
                .thenReturn(List.of(note));

        when(noteMapper.toDTO(note))
                .thenReturn(noteDTO);

        List<NoteDTO> result =
                noteService.searchNotesByTitle("java");

        assertEquals(1, result.size());
        assertEquals("Java", result.get(0).getTitle());

        verify(noteRepository)
                .findByUserAndTitleContainingIgnoreCaseOrUserAndContentContainingIgnoreCase(
                        user,
                        "java",
                        user,
                        "java"
                );

        verify(noteMapper).toDTO(note);
    }

    @Test
    void searchNotesByCategory_shouldReturnNotes() {

        when(userRepository.findByUsername("aryan"))
                .thenReturn(Optional.of(user));

        when(noteRepository
                .findByUserAndCategoryContainingIgnoreCase(
                        user,
                        "Programming"
                ))
                .thenReturn(List.of(note));

        when(noteMapper.toDTO(note))
                .thenReturn(noteDTO);

        List<NoteDTO> result =
                noteService.searchNotesByCategory("Programming");

        assertEquals(1, result.size());
        assertEquals("Programming", result.get(0).getCategory());

        verify(noteRepository)
                .findByUserAndCategoryContainingIgnoreCase(
                        user,
                        "Programming"
                );

        verify(noteMapper).toDTO(note);
    }

    @Test
    void getAllNotes_shouldReturnPaginatedNotes() {

        when(userRepository.findByUsername("aryan"))
                .thenReturn(Optional.of(user));

        PageImpl<Note> page =
                new PageImpl<>(List.of(note));

        when(noteRepository.findByUser(
                eq(user),
                any(PageRequest.class)
        )).thenReturn(page);

        when(noteMapper.toDTO(note))
                .thenReturn(noteDTO);

        var result = noteService.getAllNotes(
                0,
                5,
                "createdAt",
                "desc"
        );

        assertEquals(1, result.getTotalElements());
        assertEquals("Java", result.getContent().get(0).getTitle());

        verify(userRepository).findByUsername("aryan");

        verify(noteRepository).findByUser(
                eq(user),
                any(PageRequest.class)
        );

        verify(noteMapper).toDTO(note);
    }
}