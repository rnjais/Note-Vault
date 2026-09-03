package com.Note_Vault.repository;

import com.Note_Vault.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {


    List<Note> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(
            String title,
            String content
    );

    List<Note> findByCategoryContainingIgnoreCase(
            String category
    );
}