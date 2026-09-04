package com.Note_Vault.controller;

import com.Note_Vault.dto.NoteDTO;
import com.Note_Vault.response.ApiResponse;
import com.Note_Vault.service.NoteService;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

@RestController
@RequestMapping("/api/notes")
@Tag(
        name = "Notes",
        description = "APIs for managing notes"
)
public class NoteController {

    private final NoteService noteService;

    // Inject NoteService using constructor injection
    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }


    // =========================
    // CREATE NOTE
    // =========================
    @PostMapping
    @Operation(
            summary = "Create a new note",
            description = "Creates and saves a new note"
    )
    public ResponseEntity<ApiResponse<NoteDTO>> createNote(
            @Valid @RequestBody NoteDTO noteDTO) {

        NoteDTO savedNote = noteService.createNote(noteDTO);

        ApiResponse<NoteDTO> response = new ApiResponse<>(
                true,
                "Note created successfully",
                savedNote
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    // =========================
    // GET ALL NOTES
    // =========================
    @GetMapping
    @Operation(
            summary = "Get all notes",
            description = "Retrieves all notes with pagination and sorting"
    )
    public ResponseEntity<ApiResponse<Page<NoteDTO>>> getAllNotes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        Page<NoteDTO> notesDTO =
                noteService.getAllNotes(page, size, sortBy, direction);

        ApiResponse<Page<NoteDTO>> response = new ApiResponse<>(
                true,
                "Notes retrieved successfully",
                notesDTO
        );

        return ResponseEntity.ok(response);
    }


    // =========================
    // GET NOTE BY ID
    // =========================
    @GetMapping("/{id}")
    @Operation(
            summary = "Get note by ID",
            description = "Retrieves a single note using its ID"
    )
    public ResponseEntity<ApiResponse<NoteDTO>> getNoteById(
            @PathVariable Long id) {

        NoteDTO noteDTO = noteService.getNoteById(id);

        ApiResponse<NoteDTO> response = new ApiResponse<>(
                true,
                "Note retrieved successfully",
                noteDTO
        );

        return ResponseEntity.ok(response);
    }


    // =========================
    // UPDATE NOTE
    // =========================
    @PutMapping("/{id}")
    @Operation(
            summary = "Update a note",
            description = "Updates an existing note using its ID"
    )
    public ResponseEntity<ApiResponse<NoteDTO>> updateNote(
            @PathVariable Long id,
            @Valid @RequestBody NoteDTO noteDTO) {

        NoteDTO updatedNote =
                noteService.updateNote(id, noteDTO);

        ApiResponse<NoteDTO> response = new ApiResponse<>(
                true,
                "Note updated successfully",
                updatedNote
        );

        return ResponseEntity.ok(response);
    }


    // =========================
    // DELETE NOTE
    // =========================
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a note",
            description = "Deletes an existing note using its ID"
    )
    public ResponseEntity<ApiResponse<String>> deleteNoteById(
            @PathVariable Long id) {

        noteService.deleteNoteById(id);

        ApiResponse<String> response = new ApiResponse<>(
                true,
                "Note deleted successfully",
                null
        );

        return ResponseEntity.ok(response);
    }


    // =========================
    // SEARCH NOTES
    // =========================
    @GetMapping("/search")
    @Operation(
            summary = "Search notes",
            description = "Searches notes by title or content"
    )
    public ResponseEntity<ApiResponse<List<NoteDTO>>> searchNotesByTitle(
            @RequestParam String keyword) {

        List<NoteDTO> notesDTO =
                noteService.searchNotesByTitle(keyword);

        ApiResponse<List<NoteDTO>> response = new ApiResponse<>(
                true,
                "Search completed successfully",
                notesDTO
        );

        return ResponseEntity.ok(response);
    }


    // =========================
    // SEARCH BY CATEGORY
    // =========================
    @GetMapping("/category")
    @Operation(
            summary = "Search notes by category",
            description = "Retrieves notes matching the specified category"
    )
    public ResponseEntity<ApiResponse<List<NoteDTO>>> searchNotesByCategory(
            @RequestParam String category) {

        List<NoteDTO> notesDTO =
                noteService.searchNotesByCategory(category);

        ApiResponse<List<NoteDTO>> response = new ApiResponse<>(
                true,
                "Category search completed successfully",
                notesDTO
        );

        return ResponseEntity.ok(response);
    }
}