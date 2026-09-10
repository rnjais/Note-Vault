package com.Note_Vault.repository;

import com.Note_Vault.entity.Note;
import com.Note_Vault.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {

    Page<Note> findByUser(User user, Pageable pageable);

    List<Note> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(
            String title,
            String content
    );

    List<Note> findByCategoryContainingIgnoreCase(
            String category
    );
}