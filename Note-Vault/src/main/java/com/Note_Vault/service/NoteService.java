package com.Note_Vault.service;
import com.Note_Vault.entity.Note;
import com.Note_Vault.exception.NoteNotFoundException;
import com.Note_Vault.repository.NoteRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
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
    public Page<Note> getAllNotes(int page, int size, String sortBy, String direction) {

        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return noteRepository.findAll(pageable);
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
    //Search By Keyword (title/content)
    public List<Note> searchNotesByTitle(String keyword){
        return noteRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(keyword,keyword);
    }
    //Search By Category
    public List<Note> searchNotesByCategory(String category){
        return noteRepository.findByCategoryContainingIgnoreCase(category);
    }

}